# 📱 Xchange App — Documentação do Tech Stack

> **Última atualização:** 31 de março de 2026  
> **Módulo:** Frontend Mobile  
> **Caminho:** `app/xchange_app/`

---

## 📐 Visão Geral

Aplicativo mobile multiplataforma (Android, iOS e Web) construído com **React Native** e **Expo**, voltado para investidores de cripto. Utiliza um sistema de estilização moderno baseado em **Tailwind CSS v4** adaptado para React Native via **Uniwind**.

---

## 🧱 Stack Principal

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Framework** | React Native | `0.83.4` |
| **Plataforma** | Expo (canary) | `55.0.10` |
| **Linguagem** | TypeScript | `~5.9.2` |
| **React** | React | `19.2.0` |
| **Runtime / Package Manager** | Bun | — |
| **Bundler** | Metro | — |

> ⚠️ O projeto utiliza versões **canary** do Expo SDK 55. Essas versões são experimentais e podem conter breaking changes. Consulte as [release notes do Expo](https://expo.dev/changelog) antes de atualizar.

---

## 🧭 Navegação

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Expo Router** | `55.0.9-canary` | Roteamento file-based (inspirado no Next.js) |
| **React Navigation Native** | `^7.1.33` | Base de navegação nativa |
| **React Native Screens** | `~4.23.0` | Telas nativas otimizadas |

O projeto utiliza **Typed Routes** (`experiments.typedRoutes: true` em `app.json`), garantindo tipagem estática nas rotas.

---

## 🎨 Estilização

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Tailwind CSS** | `^4.2.2` | Framework CSS utility-first (v4 — nova API com `@theme`) |
| **Uniwind** | `^1.6.1` | Bridge que permite usar classes Tailwind no React Native |
| **PostCSS** | `^8.5.8` | Processador CSS (pipeline de build) |
| **@tailwindcss/postcss** | `^4.2.2` | Plugin Tailwind para PostCSS v4 |
| **Tailwind Variants** | `^3.2.2` | Variantes tipadas para componentes (ex: `Button`) |
| **Tailwind Merge** | `^3.5.0` | Merge inteligente de classes Tailwind |

O sistema de estilização usa **Tailwind CSS v4** com `@theme` para design tokens (cores, fontes). O Metro é configurado via `withUniwindConfig` no `metro.config.js` para processar o CSS e gerar tipagens automáticas em `uniwind-types.d.ts`.

### Paleta de Cores (Design Tokens)

| Token | Valor | Uso |
|-------|-------|-----|
| `--color-background` | `#000000` | Fundo principal |
| `--color-background-muted` | `#1a1a1a` | Fundo secundário |
| `--color-foreground` | `#ffffff` | Texto principal |
| `--color-foreground-muted` | `#a3a3a3` | Texto secundário |
| `--color-primary` | `#8b5cf6` | Cor de destaque (violeta) |
| `--color-primary-muted` | `#7c3aed` | Variante escura do primary |
| `--color-primary-foreground` | `#ffffff` | Texto sobre primary |
| `--color-accent-cyan` | `#00D4FF` | Acento ciano (gradientes) |
| `--color-accent-violet` | `#7B2FFF` | Acento violeta (gradientes) |

---

## ✍️ Tipografia

| Font Family | Pesos Disponíveis |
|------------|-------------------|
| **Sora** (Google Fonts) | Thin (100), ExtraLight (200), Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700), ExtraBold (800) |

As fontes são carregadas **nativamente em build time** via config plugin do `expo-font` (listadas no `app.json`), seguindo a best practice de não usar `loadAsync` em runtime.

---

## 🎬 Animações & Interações

| Tecnologia | Descrição |
|------------|-----------|
| **React Native Reanimated** | Animações de alta performance executadas no UI thread |
| **React Native Gesture Handler** | Gestos nativos (tap, swipe, pan) |

Animações utilizadas no projeto: `FadeIn`, `FadeInDown`, `FadeInUp`, `withSpring`, `withTiming`, `withRepeat`, `withSequence`, `useAnimatedStyle`, `useSharedValue`.

---

## 📦 Módulos Expo

| Módulo | Versão | Descrição |
|--------|--------|-----------|
| `expo-linear-gradient` | `55.0.10-canary` | Gradientes lineares nativos |
| `expo-font` | `55.0.5-canary` | Carregamento de fontes customizadas |
| `expo-constants` | `55.0.10-canary` | Constantes do app (versão, etc.) |
| `expo-splash-screen` | `55.0.14-canary` | Splash screen nativa |
| `expo-status-bar` | `55.0.5-canary` | Controle da status bar |
| `expo-linking` | `55.0.10-canary` | Deep links |
| `expo-web-browser` | `55.0.11-canary` | Browser in-app |

---

## 🌐 Suporte Web

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| `react-dom` | `19.2.0` | Renderização web |
| `react-native-web` | `~0.21.0` | Componentes RN para web |

O bundler web é o **Metro** (não Webpack), com output estático configurado em `app.json`.

---

## 🗂️ Estrutura de Pastas

```
app/xchange_app/
├── src/
│   ├── app/                    # Rotas (file-based routing via Expo Router)
│   │   └── index.tsx           # Tela inicial (landing)
│   ├── components/
│   │   └── ui/                 # Design system (componentes base)
│   │       └── button.tsx      # Componente Button com variants
│   ├── global.css              # Design tokens + Tailwind config
│   └── uniwind-types.d.ts      # Tipos auto-gerados pelo Uniwind
├── assets/
│   └── images/                 # Assets estáticos (ícones, logo, splash)
├── .agents/
│   └── skills/                 # Skills de IA para desenvolvimento
│       ├── frontend-design/    # Diretrizes de design premium
│       └── vercel-react-native-skills/  # Best practices RN
├── app.json                    # Configuração Expo
├── metro.config.js             # Config Metro + Uniwind
├── postcss.config.mjs          # Config PostCSS
├── tsconfig.json               # Config TypeScript (strict, path aliases)
├── package.json                # Dependências
└── bun.lock                    # Lockfile do Bun
```

---

## 🛠️ Ferramentas de Desenvolvimento

| Ferramenta | Uso |
|------------|-----|
| **Bun** | Package manager e runtime |
| **Metro** | Bundler React Native (configurado com Uniwind) |
| **TypeScript** | Tipagem estática (`strict: true`) |
| **VS Code** | IDE (configurações no `.vscode/`) |

### AI Skills

| Skill | Fonte | Descrição |
|-------|-------|-----------|
| `frontend-design` | `anthropics/skills` | Diretrizes de design premium para interfaces |
| `vercel-react-native-skills` | `vercel-labs/agent-skills` | Best practices React Native (performance, animações, UI) |

---

## 🚀 Como Rodar

```bash
cd app/xchange_app

# Instalar dependências
bun install

# Iniciar Expo dev server
bun start

# Rodar em plataformas específicas
bun run android    # Android
bun run ios        # iOS
bun run web        # Navegador
```

---

## 📊 Resumo de Versões

| Tecnologia | Versão |
|-----------|--------|
| React Native | `0.83.4` |
| Expo SDK | `55 (canary)` |
| React | `19.2.0` |
| TypeScript | `5.9.2` |
| Tailwind CSS | `4.2.2` |
| Uniwind | `1.6.1` |
| Bun | latest |
