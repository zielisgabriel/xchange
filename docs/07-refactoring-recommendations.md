# 7. Recomendações de Refatoração (alinhadas a DDD)

[← Voltar ao índice](./README.md)

Cada recomendação tem prioridade (🔴 alta / 🟡 média / 🟢 baixa), o problema
observado no código e a direção sugerida. Referências cruzadas a partir dos demais
documentos usam os identificadores `R1`..`R8`.

---

## R1
### 🟡 Desacoplar `Profile` (Portfolio) de `AuthUser` (Identity) {#r1}

**Problema:** o aggregate `Profile` compõe `AuthUser` e os favoritos com PK
compartilhada (`@MapsId`), fundindo dois Bounded Contexts numa fronteira
transacional única (ADR-001).
**Sugestão:** manter `AuthUser` como aggregate de **Identity & Access** e, em
**User Portfolio**, referenciar o usuário por um VO `UserId` (sem `@OneToOne`
bidirecional). A consistência entre contextos passa a ser por **referência de
identidade**, não por composição. Favoritos viram aggregate próprio do Portfolio.

## R2
### 🟡 Introduzir Input Ports explícitos para os Use Cases {#r2}

**Problema:** controllers dependem das **classes concretas** dos use cases; não há
porta de entrada (interface) — quebra parcial do hexágono.
**Sugestão:** definir interfaces `application/port/in` (ex.: `RegisterUser`,
`AddFavoriteCoin`) implementadas pelos use cases; controllers passam a depender da
interface. Facilita testes e troca de implementação.

## R3
### 🟡 Criar o Value Object `Price` e parar de trafegar preço como `String` {#r3}

**Problema:** preços circulam como `String` já formatada (`"$50,000.00"`) nos read
models de Market Data; formatação e dado estão misturados (ADR-005). Não existe o
`Price` previsto.
**Sugestão:** `Price { BigDecimal amount; Currency currency; Instant asOf }`,
imutável, com `formatted()` derivado. A formatação para `String` ocorre apenas na
borda de apresentação (DTO de resposta), não no domínio.

## R4
### 🔴 Endurecer o modelo de tokens (rotação/`kid` e revogação) {#r4}

**Problema:** chave HMAC simétrica única, sem `kid`/rotação (ADR-003); sem
revogação imediata de access tokens (ADR-002).
**Sugestão:**
- Migrar para par assimétrico (RSA/EC) com `kid` e endpoint **JWKS** se houver
  múltiplos Resource Servers; ou manter HMAC mas com **rotação versionada** por `kid`.
- Avaliar uma allowlist/denylist de `jti` no Redis (já presente para refresh) para
  permitir *logout* e revogação de access tokens comprometidos — exatamente o
  recurso descrito no briefing e ainda ausente.

## R5
### 🟢 Mover `Currency`/`Usd` para `domain.valueobject` e mover a expiração do RefreshToken para o domínio {#r5}

**Problema:** `Currency`/`Usd` estão em `domain.entities` apesar de serem VOs; a
regra de expiração do refresh token vive em `RefreshTokenRedis` (infra).
**Sugestão:** reclassificar os VOs; mover o cálculo de expiração para a entidade de
domínio `RefreshToken` (ou um Domain Service/Factory), deixando a entidade Redis só
como representação de persistência.

## R6
### 🟡 Tornar as asserções de persistência dos testes de integração reais {#r6}

**Problema:** os testes de integração rodam em `@Transactional` e leem via
`findById` (PK), que pode ser servido pelo cache de 1º nível do Hibernate sem
flush — podem passar mesmo com mapeamento quebrado (ADR-010). A cobertura real vem
dos E2E.
**Sugestão:** entre a escrita e a leitura, fazer `flush()` + `clear()` da persistence
context (via `TestEntityManager`) ou abandonar `@Transactional` e limpar tabelas
como na base E2E.

## R7
### 🟢 Extrair a política de Refresh Token como Domain Service {#r7}

**Problema:** a regra "no máximo um refresh por usuário" está dividida entre
`CreateRefreshTokenUseCase` e `RefreshTokenService` (infra).
**Sugestão:** um `RefreshTokenPolicy` no domínio de Identity & Access concentra a
regra; o use case orquestra e a infra só persiste.

## R8
### 🟢 Introduzir Domain Events para os momentos de negócio {#r8}

**Problema:** transições relevantes (`UserRegistered`, `OnboardingFinished`,
`CoinAddedToFavorites`) são chamadas síncronas diretas; não há eventos.
**Sugestão:** publicar Domain Events (via `ApplicationEventPublisher` in-process,
evoluindo para mensageria) para desacoplar consumidores (predição, analytics,
comunicação) — ver [Event Storming](./01-strategic-design.md#14-event-storming--big-picture).

---

## Resumo priorizado

| ID | Prioridade | Tema | Documento de origem |
|----|-----------|------|---------------------|
| [R4](#r4) | 🔴 | Rotação/`kid` + revogação de tokens | [ADR-002](./05-adr.md#adr-002), [ADR-003](./05-adr.md#adr-003) |
| [R1](#r1) | 🟡 | Desacoplar Identity × Portfolio | [ADR-001](./05-adr.md#adr-001) |
| [R2](#r2) | 🟡 | Input Ports explícitos | [ADR-006](./05-adr.md#adr-006) |
| [R3](#r3) | 🟡 | Value Object `Price` | [ADR-005](./05-adr.md#adr-005) |
| [R6](#r6) | 🟡 | Persistência real nos testes de integração | [ADR-010](./05-adr.md#adr-010) |
| [R5](#r5) | 🟢 | Reclassificar VOs / expiração no domínio | [Tactical 2.3](./02-tactical-design.md#23-value-objects) |
| [R7](#r7) | 🟢 | `RefreshTokenPolicy` (Domain Service) | [Tactical 2.5](./02-tactical-design.md#25-domain-services) |
| [R8](#r8) | 🟢 | Domain Events | [Strategic 1.4](./01-strategic-design.md#14-event-storming--big-picture) |
