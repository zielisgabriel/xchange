# 2. Tactical Design — xchange

[← Voltar ao índice](./README.md)

Sumário:
- [2.1 Aggregates & Aggregate Roots](#21-aggregates--aggregate-roots)
- [2.2 Entities](#22-entities)
- [2.3 Value Objects](#23-value-objects)
- [2.4 Domain Events](#24-domain-events)
- [2.5 Domain Services](#25-domain-services)
- [2.6 Repository Interfaces (Domain Ports)](#26-repository-interfaces-domain-ports)
- [2.7 Factories](#27-factories)

Pacote-base do domínio: `br.com.xchange.api.domain`.

---

## 2.1 Aggregates & Aggregate Roots

### Aggregate: Usuário (Profile)
**Aggregate Root:** `Profile`
**Invariantes protegidas:**
- Um perfil tem **no máximo 5** moedas favoritas (`Profile.MAX_FAVORITE_COINS = 5`).
- Não há favoritos **duplicados** por `coinId` (`addFavoriteCoin` ignora repetidos).
- A conclusão do onboarding altera o estado do `AuthUser` (`onboardingFinished`).
**Entidades internas:** `AuthUser` (identidade/credenciais).
**Value Objects internos:** `FavoriteCoin` (×N), e, dentro de `AuthUser`, `Cpf` e `BirthDate`.
**Consistência transacional:** registro (`AuthUser` + `Profile`) e mutações de
favoritos ocorrem numa única transação (`@Transactional` em `RegisterUserUseCase`
e `ProfileRepositoryAdapter.save`). O `Profile` e seu `AuthUser` formam **uma
fronteira transacional**.

> `[INCONSISTÊNCIA DDD]` Este aggregate mistura **identidade** (`AuthUser`) e
> **portfólio** (`FavoriteCoin`). O ideal DDD seria dois aggregates ligados por
> referência de identidade (`UserId`), não por composição direta. Ver
> [refatoração](./07-refactoring-recommendations.md#r1).

### Aggregate: RefreshToken
**Aggregate Root:** `RefreshToken`
**Invariantes protegidas:**
- Um Refresh Token tem `id` (UUID) e referencia `userId`.
- Expira em **15 dias** (`RefreshTokenRedis` `@TimeToLive`).
- Há **no máximo um** refresh token por usuário no fluxo de criação
  (`CreateRefreshTokenUseCase` rejeita se já existe — `RefreshTokenAlreadyExistsException`).
**Entidades internas:** nenhuma.
**Value Objects internos:** nenhum (`expiration` é `Long` em segundos).
**Consistência transacional:** Redis (chave por `id`, índice secundário por
`userId`). Sem transação relacional — store key-value com TTL.

### Aggregates de leitura (não-transacionais) — Market Data & Predição
Os objetos de Market Data (`CoinWithMarketData`, `CoinDetailData`,
`CoinChartData`, `GlobalCoinMetricsData`) e de Predição (`CoinPrediction`,
`FavoriteCoinWithPrediction`) **não são aggregates transacionais**: são **read
models** montados a partir de fontes externas (CoinGecko/IA), sem persistência
própria nem invariantes de escrita. `[INFERIDO]` — tratados como aggregates só no
sentido de coesão de leitura.

---

## 2.2 Entities

### `AuthUser` — `domain.entities.AuthUser`
| Atributo | Tipo |
|---|---|
| `id` | `UUID` |
| `profile` | `Profile` |
| `firstName` / `lastName` | `String` |
| `email` | `String` |
| `password` | `String` (hash Argon2) |
| `birthDate` | `BirthDate` (VO) |
| `cpf` | `Cpf` (VO) |
| `onboardingFinished` | `boolean` |

- **Identidade:** `UUID` gerado pelo banco (`@GeneratedValue(strategy = UUID)` em `AuthUserJpa`).
- **Comportamento de domínio:** `finishOnboarding()` — transição que marca o usuário como tendo concluído o onboarding.
- **Ciclo de vida:** `registrado (onboardingFinished=false)` → `onboarding concluído (true)`.

> `[INCONSISTÊNCIA DDD]` `AuthUser` é um `@Data` (Lombok) com setters públicos —
> entidade anêmica. Regras de validação (CPF, idade) moram nos VOs, mas o estado é
> totalmente mutável de fora.

### `Profile` — `domain.entities.Profile`
| Atributo | Tipo |
|---|---|
| `id` | `UUID` |
| `authUser` | `AuthUser` |
| `favoriteCoins` | `Set<FavoriteCoin>` |
| `MAX_FAVORITE_COINS` | `static final int = 5` |

- **Identidade:** mesmo `UUID` do `AuthUser` (PK compartilhada via `@MapsId`).
- **Comportamento de domínio (rico):**
  - `addFavoriteCoin(FavoriteCoin)` — ignora duplicata por `coinId`; lança `FavoriteCoinsLimitExceededException` se já há 5.
  - `removeFavoriteCoin(String coinId)` — remove por `coinId`.
- **Ciclo de vida:** criado vazio no registro → recebe favoritos no onboarding → mutável (add/remove).

### `RefreshToken` — `domain.entities.RefreshToken`
| Atributo | Tipo |
|---|---|
| `id` | `UUID` (é o token entregue ao cliente) |
| `userId` | `UUID` |
| `expiration` | `Long` (segundos) |

- **Identidade:** `UUID` gerado no construtor de `RefreshTokenRedis`.
- **Comportamento de domínio:** nenhum método de negócio na entidade de domínio (a regra de expiração está no `@TimeToLive` da entidade Redis — `[INCONSISTÊNCIA DDD]`: regra de expiração vazou para a camada de infraestrutura).
- **Ciclo de vida:** `criado` → `expira após 15 dias (TTL Redis)` ou `validado em refresh`.

---

## 2.3 Value Objects

### `Cpf` — `domain.valueobject.Cpf`
- **Atributos:** `String value` (apenas dígitos).
- **Invariantes no construtor:** não nulo; 11 dígitos; não todos iguais; dígitos verificadores válidos. Lança `CpfException`.
- **Imutabilidade:** `@Value` (Lombok) — final, sem setters. Normaliza removendo pontuação.

### `BirthDate` — `domain.valueobject.BirthDate`
- **Atributos:** `LocalDate value`.
- **Invariantes no construtor:** não pode ser futura; idade ≥ 18 anos. Lança `BirthDateException`.
- **Imutabilidade:** `@Value` — encapsula a regra de maioridade num único lugar.

### `Currency` (abstrata) / `Usd` — `domain.entities.Currency`, `domain.entities.currencies.Usd`
- **Atributos:** `BigDecimal value`.
- **Comportamento:** `getSymbol()`, `getCode()`, `formatted(min, max)`, `parseFromString()`. `Usd` formata em `Locale.US` (`$`, `USD`) e faz parsing removendo `$`/`,`.
- **Imutabilidade:** sem setters; valor definido no construtor. Modela dinheiro como conceito de domínio (evita `BigDecimal` cru espalhado).

> `[INCONSISTÊNCIA DDD]` `Currency`/`Usd` estão em `domain.entities.*` mas são
> conceitualmente **Value Objects**, não entidades. Recomenda-se movê-los para
> `domain.valueobject`. Além disso, **não existe o VO `Price`** previsto no
> briefing — preços trafegam como `String` já formatada (ex.: `"$50,000.00"`) nos
> read models de Market Data, o que mistura formatação com dado.

### `FavoriteCoin` — `domain.entities.FavoriteCoin`
- **Atributos:** `coinId`, `name`, `symbol`, `imageUrl` (todos `String`).
- **Natureza:** sem identidade própria; igualdade por valor (`@Data`); persistido como `@Embeddable` (`FavoriteCoinsJpa`) numa `@ElementCollection`. **Comporta-se como Value Object** dentro do aggregate `Profile`. `[INFERIDO]` classificado como VO embora resida em `entities`.

---

## 2.4 Domain Events

> **Não há Domain Events no código.** Esta seção documenta os eventos
> **candidatos** e o contrato (payload) sugerido caso sejam introduzidos. Marcado
> integralmente como `[INFERIDO]`. A lista de gatilhos está em
> [Event Storming](./01-strategic-design.md#14-event-storming--big-picture).

### `UserRegistered` `[INFERIDO]`
**Bounded Context:** Identity & Access
**Disparado quando:** um `AuthUser` + `Profile` são persistidos com sucesso.
**Payload:** `userId`, `email`, `firstName`, `occurredAt`.
**Consumidores:** Onboarding (iniciar), comunicação (boas-vindas).
**Entrega sugerida:** `ApplicationEventPublisher` (in-process) → handler assíncrono.

### `OnboardingFinished` `[INFERIDO]`
**Bounded Context:** User Portfolio
**Disparado quando:** `FinishOnboardingUseCase` conclui e marca `onboardingFinished=true`.
**Payload:** `userId`, `favoriteSymbols[]`, `occurredAt`.
**Consumidores:** Coin Prediction (pré-aquecer predições), analytics.
**Entrega sugerida:** assíncrona.

### `CoinAddedToFavorites` / `CoinRemovedFromFavorites` `[INFERIDO]`
**Bounded Context:** User Portfolio
**Disparado quando:** `AddFavoriteCoinUseCase` / `RemoveFavoriteCoinUseCase` alteram o `Set<FavoriteCoin>`.
**Payload:** `userId`, `coinId`, `symbol`, `occurredAt`.
**Consumidores:** Coin Prediction, notificações [futuro].
**Entrega sugerida:** assíncrona.

### `AccessTokenRefreshed` `[INFERIDO]`
**Bounded Context:** Identity & Access
**Disparado quando:** `RefreshAccessTokenUseCase` valida o refresh e emite novo Access Token.
**Payload:** `userId`, `refreshTokenId`, `occurredAt`.
**Consumidores:** auditoria/segurança.
**Entrega sugerida:** assíncrona.

---

## 2.5 Domain Services

> **Quase não há Domain Services puros.** A lógica de domínio mora nas entidades
> (`Profile.addFavoriteCoin`) e nos VOs (`Cpf`, `BirthDate`). Os "services" do
> código são **Application Services** (use cases) ou **Infrastructure Services**.

| Classe | Camada real | Papel | Classificação correta |
|---|---|---|---|
| `RefreshTokenService` (`infra.services`) | Infra/App | Cria ou recupera refresh token; valida (token existe + usuário existe). | **Application/Domain Service híbrido** — contém regra "1 refresh por usuário". Candidato a Domain Service de Identity & Access. |
| `JwtTokenService` (`infra.services`) | Infra | Gera/valida JWT (HMAC). | **Infrastructure Service** (implementa `AccessTokenServicePort`). |
| `XchangeUserDetailsService` (`infra.services`) | Infra | Carrega usuário p/ Spring Security. | Infrastructure (adapter do Spring Security). |
| `PrincipalUtils` (`application.utils`) | App | Extrai `UUID` do `Principal`. | Utilitário (não é Domain Service). |

> `[INFERIDO]` A regra "**no máximo um refresh token por usuário**"
> (`CreateRefreshTokenUseCase` + `RefreshTokenService`) é lógica de domínio de
> Identity & Access que hoje vive espalhada entre use case e infra. Seria um
> **Domain Service** legítimo (`RefreshTokenPolicy`) se extraída para o domínio.

---

## 2.6 Repository Interfaces (Domain Ports)

Portas de saída reais em `domain.ports.repositories` e `domain.ports.services`
(sem dependência de JPA/Redis/HTTP).

```java
// domain.ports.repositories.AuthUserRepositoryPort
public interface AuthUserRepositoryPort {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findById(UUID id);
    AuthUser save(AuthUser authUser);
}

// domain.ports.repositories.ProfileRepositoryPort
public interface ProfileRepositoryPort {
    Optional<Profile> findById(UUID id);
    Profile save(Profile profile);
}

// domain.ports.repositories.RefreshTokenRepositoryPort
public interface RefreshTokenRepositoryPort {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findById(UUID id);
    Optional<RefreshToken> findByUserId(UUID userId);
}
```

Portas de **serviço** (saída) — externas ao domínio, mas declaradas em
`domain.ports.services`:

```java
// domain.ports.services.CoinServicePort  (implementada por CoinGeckoServiceAdapter)
public interface CoinServicePort {
    List<Coin> getTrendingCoins();
    List<CoinWithMarketData> getCoinsWithMarketData();
    List<CoinWithMarketData> getTrendingCoinsDetailed();
    GlobalCoinMetricsData getGlobalCoinMetrics();
    CoinChartData getChartDataById(String coinId);
    CoinDetailData getCoinDetailById(String coinId);
    List<Coin> getCoinsByQuery(String query);
}

// domain.ports.services.RecommendationCoinServicePort  (impl: CryptoRecommendationAIServiceAdapter)
public interface RecommendationCoinServicePort {
    Set<CoinPrediction> getCoinPredictionBySymbols(List<String> symbols);
}

// domain.ports.services.AccessTokenServicePort  (impl: JwtTokenService)
public interface AccessTokenServicePort {
    String generate(Object principal);
    String generateFromUserId(UUID userId);
    String getSubject(String token);
    List<Object> getAuthorities(String token);
    boolean validate(String token);
}

// domain.ports.services.PasswordEncoderPort  (impl: SpringPasswordEncoderAdapter)
// domain.ports.services.RefreshTokenServicePort (impl: RefreshTokenService)
```

> **Observação DDD:** não há uma porta de repositório dedicada a `Coin` (como
> `CoinRepository`) porque Market Data não persiste moedas — consome o CoinGecko
> sob demanda com cache Redis. Portanto a "porta de saída" de Market Data é o
> **service port** `CoinServicePort`, não um repository. Isso é coerente com o fato
> de Market Data ser um contexto de **leitura** sobre fonte externa.

---

## 2.7 Factories

> **Não há Factories DDD explícitas.** A construção de aggregates é feita via
> construtores + setters (Lombok) e via **mappers/assemblers** que cumprem papel
> parcial de factory:

| Mecanismo | Local | Papel |
|---|---|---|
| `AuthUserJpa.fromDomain/toDomain` | `infra.entities` | Reconstitui `AuthUser` de/para persistência. |
| `ProfileJpa.fromDomain/toDomain` | `infra.entities` | Reconstitui `Profile` (com `AuthUser` e favoritos). |
| `RefreshTokenRedis.fromDomain/toDomain` | `infra.entities` | Reconstitui `RefreshToken`. |
| `*Request.toDomain()` (DTOs) | `application.dto.request` | Cria entidades de domínio a partir do request (ex.: `RegisterUserRequest.toDomain()` cria `AuthUser` com `Cpf`/`BirthDate`). |
| `*Response.fromDomain()` (DTOs) | `application.dto.response` | Monta DTO de saída a partir do domínio. |

> `[INFERIDO]` Uma `Factory` legítima seria útil para `RefreshToken` (encapsular
> geração de `id` + cálculo de expiração, hoje no construtor de `RefreshTokenRedis`,
> na infraestrutura) e para a reconstituição de `Profile` com seu `AuthUser`
> (lógica de montagem repetida entre `ProfileJpa.toDomain` e `AuthUserJpa.toDomain`).
