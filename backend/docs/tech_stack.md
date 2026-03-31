# ⚙️ Xchange API — Documentação do Tech Stack

> **Última atualização:** 31 de março de 2026  
> **Módulo:** Backend REST API  
> **Caminho:** `backend/xchange_api/`  
> **Group ID:** `br.com.xchange`

---

## 📐 Visão Geral

API REST construída com **Spring Boot 4** e **Java 25**, seguindo uma **Arquitetura Hexagonal (Ports & Adapters)**. Responsável pela autenticação (JWT), gerenciamento de usuários e regras de negócio da plataforma Xchange.

---

## 🧱 Stack Principal

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Framework** | Spring Boot | `4.0.4` |
| **Linguagem** | Java | `25` |
| **Build Tool** | Maven | (Maven Wrapper incluso) |
| **Banco de Dados** | PostgreSQL | `17-alpine` |
| **Containerização** | Docker Compose | — |

---

## 📚 Dependências Spring Boot

### Starters

| Starter | Descrição |
|---------|-----------|
| `spring-boot-starter-webmvc` | API REST com Spring MVC |
| `spring-boot-starter-data-jpa` | ORM com Hibernate / JPA |
| `spring-boot-starter-security` | Autenticação e autorização |
| `spring-boot-starter-validation` | Validação de beans com Jakarta Validation |
| `spring-boot-devtools` | Hot reload em desenvolvimento (runtime, optional) |

### Starters de Teste

| Starter | Descrição |
|---------|-----------|
| `spring-boot-starter-data-jpa-test` | Testes de repositório |
| `spring-boot-starter-security-test` | Testes de segurança |
| `spring-boot-starter-validation-test` | Testes de validação |
| `spring-boot-starter-webmvc-test` | Testes de controllers |

---

## 🔐 Segurança & Autenticação

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Spring Security** | (gerenciado pelo Boot) | Framework de segurança completo |
| **JJWT — jjwt-api** | `0.13.0` | API para criação e validação de tokens JWT |
| **JJWT — jjwt-impl** | `0.13.0` | Implementação do JJWT (runtime) |
| **JJWT — jjwt-jackson** | `0.13.0` | Serialização JWT com Jackson |
| **Bouncy Castle** | `1.83` | Provider criptográfico (`bcprov-jdk18on`) |

### Fluxo de Autenticação

```
┌──────────┐     POST /auth/login      ┌───────────────────────┐
│  Client  │ ─────────────────────────▶ │   AuthController      │
│  (App)   │                            │                       │
│          │ ◀───────────────────────── │  → Authentication     │
│          │     { accessToken }        │    Provider            │
└──────────┘                            │  → JwtTokenService    │
                                        └───────────────────────┘
                                                  │
┌──────────┐     GET /api/* + Bearer    ┌─────────▼─────────────┐
│  Client  │ ─────────────────────────▶ │  AccessTokenFilter    │
│  (App)   │                            │  → valida JWT         │
│          │ ◀───────────────────────── │  → seta SecurityCtx   │
└──────────┘     200 / 401 / 403        └───────────────────────┘
```

### Filtros HTTP

| Filtro | Descrição |
|--------|-----------|
| `XchangeAuthenticationFilter` | Processa credenciais de login |
| `AccessTokenFilter` | Valida JWT em requisições autenticadas |

### Handlers de Erro

| Handler | Descrição |
|---------|-----------|
| `XchangeAuthenticationEntryPoint` | Resposta para requisições não autenticadas (401) |
| `XchangeAuthenticationFailureHandler` | Resposta para falha de autenticação |
| `XchangeAccessDeniedHandler` | Resposta para acesso negado (403) |
| `GlobalExceptionHandler` | Handler global de exceções |

---

## 🗄️ Banco de Dados

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **PostgreSQL** | `17-alpine` | Banco relacional principal |
| **PostgreSQL JDBC Driver** | (gerenciado pelo Boot) | Conexão Java ↔ PostgreSQL |
| **Hibernate** | (gerenciado pelo Boot) | ORM, com `ddl-auto: update` em dev |

### Configuração (profile `dev`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/xchange_db
    username: xchange_user
    password: xchange_password
  jpa:
    hibernate:
      ddl-auto: update
```

### Docker Compose

```yaml
services:
  xchange_psql:
    image: postgres:17-alpine
    container_name: xchange_psql_container
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: xchange_user
      POSTGRES_PASSWORD: xchange_password
      POSTGRES_DB: xchange_db
    networks:
      - xchange_network
```

---

## 🔧 Utilitários

| Tecnologia | Descrição |
|------------|-----------|
| **Lombok** | Redução de boilerplate (getters, setters, builders, construtores, etc.) |
| **Maven Wrapper** | Build sem necessidade de instalar Maven globalmente |

---

## 🏗️ Arquitetura — Hexagonal (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────────────┐
│                        Infrastructure                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌───────────────┐  │
│  │Controllers│  │ Filters  │  │ Handlers │  │    Config      │  │
│  └─────┬─────┘  └──────────┘  └──────────┘  └───────────────┘  │
│        │                                                         │
│  ┌─────▼──────────────────────────────────────────────────────┐ │
│  │                      Application                            │ │
│  │  ┌────────────────┐         ┌──────────────────────────┐   │ │
│  │  │   Use Cases     │        │       DTOs                │   │ │
│  │  │ RegisterUser... │        │ LoginRequest/Response...  │   │ │
│  │  └───────┬─────────┘        └──────────────────────────┘   │ │
│  └──────────┼─────────────────────────────────────────────────┘ │
│             │                                                    │
│  ┌──────────▼─────────────────────────────────────────────────┐ │
│  │                        Domain                               │ │
│  │  ┌──────────┐  ┌──────────────┐  ┌────────────────────┐   │ │
│  │  │ Entities  │  │ Value Objects │  │    Exceptions      │   │ │
│  │  │ AuthUser  │  │  Cpf, Birth   │  │ CpfException, etc │   │ │
│  │  └──────────┘  └──────────────┘  └────────────────────┘   │ │
│  │  ┌──────────────────────────────┐                          │ │
│  │  │   Ports (Interfaces)          │                          │ │
│  │  │ AuthUserRepositoryPort        │                          │ │
│  │  └──────────────────────────────┘                          │ │
│  └────────────────────────────────────────────────────────────┘ │
│        ▲                                                         │
│  ┌─────┴──────────────────────────────────────────────────────┐ │
│  │                   Adapters (infra)                          │ │
│  │  AuthUserRepositoryAdapter  →  JpaAuthUserRepositoryImpl   │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Camadas

| Camada | Pacote | Responsabilidade |
|--------|--------|-----------------|
| **Domain** | `domain/entities` | Entidades de negócio puras (`AuthUser`) |
| | `domain/valueobject` | Value Objects com validação (`Cpf`, `BirthDate`) |
| | `domain/exceptions` | Exceções de domínio (`CpfException`, `BirthDateException`, `EmailOrPasswordInvalidException`, `AccessTokenInvalidException`, `UserAlreadyExistsException`) |
| | `domain/ports/repositories` | Interfaces (ports) que definem contratos (`AuthUserRepositoryPort`) |
| **Application** | `application/usecase` | Casos de uso / regras de aplicação (`RegisterUserUseCase`) |
| | `application/dto/request` | DTOs de entrada (`LoginUserRequestDto`, `RegisterUserRequestDto`) |
| | `application/dto/response` | DTOs de saída (`LoginResponseDto`) |
| **Infrastructure** | `infra/controllers` | Controllers REST (`AuthController`, `TestController`) |
| | `infra/services` | Serviços de infraestrutura (`JwtTokenService`, `XchangeUserDetailsService`) |
| | `infra/providers` | Authentication providers (`XchangeAuthenticationProvider`) |
| | `infra/filters` | Filtros da cadeia HTTP (`AccessTokenFilter`, `XchangeAuthenticationFilter`) |
| | `infra/handlers` | Exception handlers e entry points |
| | `infra/adapters/repositories` | Adaptadores dos ports (`AuthUserRepositoryAdapter`) |
| | `infra/adapters/implementations` | Implementações JPA (`JpaAuthUserRepositoryImpl`) |
| | `infra/entities` | Entidades JPA mapeadas (`AuthUserJpa`, `XchangeUserDetails`) |
| | `infra/config` | Configurações Spring (`SecurityConfigDev`) |

---

## 🗂️ Estrutura de Pastas

```
backend/xchange_api/
├── src/
│   ├── main/
│   │   ├── java/br/com/xchange/api/
│   │   │   ├── ApiApplication.java
│   │   │   ├── domain/
│   │   │   │   ├── entities/          # AuthUser
│   │   │   │   ├── valueobject/       # Cpf, BirthDate
│   │   │   │   ├── exceptions/        # Exceções de domínio
│   │   │   │   └── ports/
│   │   │   │       └── repositories/  # AuthUserRepositoryPort
│   │   │   ├── application/
│   │   │   │   ├── usecase/           # RegisterUserUseCase
│   │   │   │   └── dto/
│   │   │   │       ├── request/       # LoginUserRequestDto, RegisterUserRequestDto
│   │   │   │       └── response/      # LoginResponseDto
│   │   │   └── infra/
│   │   │       ├── config/            # SecurityConfigDev
│   │   │       ├── controllers/       # AuthController, TestController
│   │   │       ├── services/          # JwtTokenService, XchangeUserDetailsService
│   │   │       ├── providers/         # XchangeAuthenticationProvider
│   │   │       ├── filters/           # AccessTokenFilter, XchangeAuthenticationFilter
│   │   │       ├── handlers/          # Exception handlers
│   │   │       ├── adapters/
│   │   │       │   ├── repositories/  # AuthUserRepositoryAdapter
│   │   │       │   └── implementations/ # JpaAuthUserRepositoryImpl
│   │   │       └── entities/          # AuthUserJpa, XchangeUserDetails
│   │   └── resources/
│   │       ├── application.yaml       # Config base (profile: dev)
│   │       └── application-dev.yaml   # Config dev (DB, JWT secret)
│   └── test/                          # Testes
├── pom.xml                            # Configuração Maven
├── mvnw                               # Maven Wrapper (Linux/Mac)
└── mvnw.cmd                           # Maven Wrapper (Windows)
```

---

## 🚀 Como Rodar

### 1. Subir o banco de dados

```bash
# Na raiz do projeto (xchange/)
docker compose up -d
```

### 2. Rodar a API

```bash
cd backend/xchange_api

# Linux / Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 📊 Resumo de Versões

| Tecnologia | Versão |
|-----------|--------|
| Java | `25` |
| Spring Boot | `4.0.4` |
| Spring Security | gerenciado pelo Boot |
| Spring Data JPA | gerenciado pelo Boot |
| Hibernate | gerenciado pelo Boot |
| JJWT | `0.13.0` |
| Bouncy Castle | `1.83` |
| PostgreSQL | `17-alpine` |
| Lombok | gerenciado pelo Boot |
| Maven | Wrapper incluso |
