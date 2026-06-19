# 5. Architecture Decision Records — xchange

[← Voltar ao índice](./README.md)

> Os ADRs abaixo descrevem decisões **observadas no código**. Quando o briefing
> pediu uma decisão que **não está implementada**, o ADR registra o estado real e
> marca a divergência como `[INFERIDO]`/lacuna, com a direção recomendada.

Índice: [001](#adr-001) · [002](#adr-002) · [003](#adr-003) · [004](#adr-004) ·
[005](#adr-005) · [006](#adr-006) · [007](#adr-007) · [008](#adr-008) ·
[009](#adr-009) · [010](#adr-010)

---

## ADR-001
### Separação de `AuthUser` e `Profile` com PK compartilhada (`@MapsId`)

**Status:** Accepted

**Contexto:**
Era preciso separar **credenciais/identidade** (e-mail, senha, CPF, data de
nascimento) dos **dados de portfólio** (moedas favoritas, onboarding), mas mantê-los
ligados ao mesmo usuário sem chave estrangeira redundante.

**Decisão:**
Modelar duas entidades JPA — `AuthUserJpa` e `ProfileJpa` — em relação `@OneToOne`
com **PK compartilhada** via `@MapsId` (o `id` do `Profile` é o mesmo do `AuthUser`).
No domínio, `Profile` agrega `AuthUser` e o `Set<FavoriteCoin>`. O registro persiste
ambos na mesma transação (`RegisterUserUseCase`).

**Consequências:**
- ✅ Identidade única e consistente entre identidade e portfólio; joins triviais.
- ✅ Onboarding e favoritos ficam coesos sob um aggregate root (`Profile`).
- ⚠️ `[INCONSISTÊNCIA DDD]` acopla **dois Bounded Contexts** (Identity & Access e
  User Portfolio) num único aggregate transacional. Evoluções de identidade e de
  portfólio passam a colidir. Ver [R1](./07-refactoring-recommendations.md#r1).

---

## ADR-002
### Refresh tokens em Redis com TTL (e não `jti` allowlist de access token)

**Status:** Accepted (com divergência do briefing)

**Contexto:**
Access tokens JWT são stateless e de curta duração (10 min). Era necessário um
mecanismo de **sessão de longa duração** que permitisse renovar o acesso sem novo
login e que pudesse ser invalidado server-side.

**Decisão:**
Persistir **refresh tokens** no Redis como `@RedisHash("refresh_token")`
(`RefreshTokenRedis`) com `@TimeToLive` de **15 dias** e índice secundário por
`userId`. O `id` (UUID) é o próprio token entregue ao cliente. A renovação
(`/auth/refresh`) valida a existência no Redis e a existência do usuário.

**Consequências:**
- ✅ Sessão revogável e expirável server-side; access token continua stateless.
- ✅ Regra "um refresh por usuário" via índice `userId`.
- ⚠️ **Divergência do briefing:** **não há `jti` allowlist** de *access tokens*. Um
  access token roubado é válido até expirar (10 min) — não há revogação imediata.
- ⚠️ `[INCONSISTÊNCIA DDD]` a regra de expiração mora na entidade de infraestrutura
  (`RefreshTokenRedis`), não no domínio.

---

## ADR-003
### Assinatura de JWT com chave simétrica única (HMAC `HS512`) — **sem rotação/`kid`**

**Status:** Accepted (lacuna face ao briefing) · `[INFERIDO]`

**Contexto:**
Os Resource paths precisam validar tokens emitidos pela própria API.

**Decisão (real):**
`JwtTokenService` assina com `Keys.hmacShaKeyFor(secret.getBytes())` — **uma única
chave simétrica** vinda de `${jwt.secret-key}`. **Não há** header `kid`, **não há**
rotação de chaves, **não há** par assimétrico (RSA/EC) nem JWKS.

**Consequências:**
- ✅ Simplicidade: emissão e validação com o mesmo segredo, sem infraestrutura de chaves.
- ⚠️ **Lacuna vs. briefing:** sem `kid`/rotação, girar a chave invalida **todos** os
  tokens de uma vez; sem chave pública, terceiros não podem validar tokens.
- ⚠️ HMAC simétrico exige que o segredo nunca saia do servidor emissor. Recomendação:
  migrar para RSA/EC + `kid` + endpoint JWKS se houver múltiplos Resource Servers.
  Ver [R4](./07-refactoring-recommendations.md#r4).

---

## ADR-004
### Login mobile via filtro de autenticação customizado (não OAuth2 grant)

**Status:** Accepted (divergência do briefing)

**Contexto:**
O app mobile precisa autenticar por e-mail/senha sem fluxo de redirect de browser.

**Decisão (real):**
Estender `UsernamePasswordAuthenticationFilter` em `XchangeAuthenticationFilter`,
processando `POST /auth/login` (corpo JSON `{email,password}`). Em sucesso, o
handler emite `LoginResponse {access_token, refresh_token}`. A validação por
requisição é feita por `AccessTokenFilter` (lê `Authorization: Bearer`).

**Consequências:**
- ✅ Sem redirect de browser — adequado a app nativo; contrato simples (JSON in/out).
- ✅ Stateless (`SessionCreationPolicy.NEVER`).
- ⚠️ **Divergência:** **não é** um *grant type OAuth2 customizado* nem usa Spring
  Authorization Server — é um filtro Spring Security próprio. Sem `client_id`,
  `scope`, nem fluxo OIDC.
- ⚠️ `[INCONSISTÊNCIA DDD]` lógica de aplicação (emissão de tokens) vive no filtro,
  não num use case.

---

## ADR-005
### CoinGecko como Open Host Service protegido por Anti-Corruption Layer

**Status:** Accepted

**Contexto:**
Os dados de mercado vêm do CoinGecko (API pública), cujo formato não deve vazar
para o domínio.

**Decisão:**
Consumir o CoinGecko via `CoinGeckoRestProvider` (HTTP + `x-cg-demo-api-key`) e
`CoinsGeckoService`, e **traduzir** os DTOs externos para o domínio em
`CoinGeckoServiceAdapter` (implementa `CoinServicePort`). Respostas são cacheadas
no Redis com TTLs por endpoint (`RedisCacheConfig`).

**Consequências:**
- ✅ Domínio isolado de mudanças do CoinGecko (ACL); preços formatados via `Usd`.
- ✅ Cache reduz custo/latência e respeita limites de rate do provedor.
- ⚠️ Lógica de apresentação (formatação de preço em `String`) ocorre na ACL — ver
  recomendação de `Price` VO ([R3](./07-refactoring-recommendations.md#r3)).

---

## ADR-006
### Arquitetura Hexagonal (Ports & Adapters) com camadas `domain`/`application`/`infra`

**Status:** Accepted

**Contexto:** Isolar regras de negócio de framework e fornecedores externos.

**Decisão:** Domínio define **ports** (`domain.ports.*`); aplicação define **use
cases**; infraestrutura provê **adapters** (JPA, Redis, HTTP) injetados pelo Spring.

**Consequências:**
- ✅ Testabilidade alta (use cases testados com mocks dos ports; integração com H2).
- ⚠️ Faltam **input ports** explícitos — controllers acoplam-se às classes
  concretas dos use cases. Ver [R2](./07-refactoring-recommendations.md#r2).

---

## ADR-007
### Microserviço de IA em Python (FastAPI) para predição de tendência

**Status:** Accepted

**Contexto:** Recomendar tendência ("alta"/"baixa") por moeda exige um modelo de ML
fora do stack Java.

**Decisão:** Serviço `crypto-recommendation-ai` (FastAPI + RandomForest) expõe
`POST /predict` (por símbolos). Atualiza recomendações em cache a cada 15 min via
`BackgroundScheduler`, coletando dados do CoinGecko. A `xchange_api` o consome via
`RecommendationCoinServicePort`/ACL.

**Consequências:**
- ✅ Separação de responsabilidade e de linguagem (Python p/ ML); falha do serviço
  degrada graciosamente (`prediction = null`).
- ⚠️ Cache em memória do processo Python (não compartilhado/escalável); endpoint sem
  autenticação. `[INFERIDO]`

---

## ADR-008
### Senhas com Argon2 via porta `PasswordEncoderPort`

**Status:** Accepted

**Contexto:** Armazenar senhas com hashing forte e moderno.

**Decisão:** `SecurityConfigDev` registra `Argon2PasswordEncoder` (defaults
Spring Security 5.8); o domínio depende de `PasswordEncoderPort`
(`SpringPasswordEncoderAdapter`).

**Consequências:**
- ✅ Hashing resistente; domínio agnóstico ao algoritmo.
- ⚠️ Argon2 é custoso por design — impacto em latência de login/registro (aceitável).

---

## ADR-009
### Cache de Market Data no Redis com TTL por endpoint

**Status:** Accepted

**Contexto:** Reduzir chamadas ao CoinGecko e latência das telas de mercado.

**Decisão:** `@Cacheable` nos endpoints de `CoinsController` com caches dedicados
(`coinsList`, `simpleCoinsList`, `trendingCoins`, `globalCoinMetrics`, `coinChart`,
`coinDetail`, `coinsByQuery`) e TTLs específicos (de 5 min a 30 dias) em
`RedisCacheConfig`.

**Consequências:**
- ✅ Menos custo/latência; respeita limites do provedor.
- ⚠️ Janela de obsolescência por endpoint; chave de cache de busca por `query` pode
  crescer (TTL de 30 dias).

---

## ADR-010
### Testes de integração com H2 + E2E com servidor real; sem Docker no CI

**Status:** Accepted

**Contexto:** O CI abandonou Docker (commit `85bf603`), então os testes não podem
depender de Postgres/Redis containerizados.

**Decisão:** Camada de **integração** com `@SpringBootTest` + MockMvc, **H2** em
memória e perfis `{dev,test}`, mockando os limites externos (CoinGecko, IA, Redis).
Camada **E2E** com `WebEnvironment.RANDOM_PORT` + `HttpClient` do JDK, exercitando
jornadas reais (registrar → logar → usar token → refresh) com persistência real e
mock estável do Redis em memória.

**Consequências:**
- ✅ Suíte roda em CI sem infraestrutura externa; cobre do HTTP ao banco.
- ⚠️ Integração com `@Transactional` valida persistência via cache de 1º nível do
  Hibernate (a cobertura real de persistência vem dos E2E). Ver
  [R6](./07-refactoring-recommendations.md#r6).
