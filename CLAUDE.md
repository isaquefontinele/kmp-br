# CLAUDE.md

Guidance for Claude Code (and other contributors) working in this repository.

## Project

`kmp-br` is an open-source (MIT) Kotlin Multiplatform library of Brazilian utilities: CPF, CNPJ
(numeric **and alphanumeric**), CEP, phones, BRL money and amounts in words, holidays and business
days, license plates and address normalization. Scope lives in [docs/REQUIREMENTS.md](docs/REQUIREMENTS.md);
planned work and milestones in [ROADMAP.md](ROADMAP.md). Work is tracked as GitHub issues that map
1:1 to ROADMAP checklist items.

## Stack

- Kotlin 2.x, Kotlin Multiplatform, Gradle with Kotlin DSL.
- Dependency and plugin versions live **only** in the version catalog `gradle/libs.versions.toml`.
- Shared build logic lives in convention plugins in the `build-logic/` included build — modules
  apply a convention plugin instead of configuring Kotlin themselves.
- Planned targets: Android, JVM, iOS (arm64, simulatorArm64), macOS, JS, Wasm (see ROADMAP Milestone 0).
- Tests: `kotlin.test` in `commonTest`; property-based tests with Kotest for check-digit algorithms.
- JDK 17+ required. Xcode 16+ for Apple targets (macOS only).

## Module layout

Each feature is its own Gradle module named `kmpbr-<feature>`, so consumers pick only what they need:

| Module | Package | Contents |
|---|---|---|
| `kmpbr-documents` | `br.kmp.documents` | CPF, CNPJ, CNH, título de eleitor, PIS/NIS, RENAVAM |
| `kmpbr-address` | `br.kmp.address` | CEP, UF, IBGE codes, address normalizer/parser |
| `kmpbr-phone` | `br.kmp.phone` | DDDs, mobile/landline, formatting, E.164 |
| `kmpbr-money` | `br.kmp.money` | `Brl` format/parse, numbers and amounts in words |
| `kmpbr-calendar` | `br.kmp.calendar` | Holidays, business-day math (may depend on `kotlinx-datetime`) |
| `kmpbr-vehicle` | `br.kmp.vehicle` | Old and Mercosul license plates |
| `kmpbr-compose` | `br.kmp.compose` | Optional Compose Multiplatform input masks |

Modules are added when their milestone starts; check `settings.gradle.kts` for what exists today.
Maven coordinates: group `io.github.isaquefontinele.kmpbr`, artifact = module name.

## Build and test

```bash
./gradlew build                      # compile and test everything available on this machine
./gradlew check                      # tests on all available targets
./gradlew :kmpbr-documents:jvmTest   # one module, one target
```

Always use the Gradle wrapper (`./gradlew`), never a system Gradle.

## Conventions

- **Code, KDoc, docs, commit messages and issues are in English.** Portuguese appears only in
  domain terms and user-facing values (e.g. "ponto facultativo", "mil reais").
- **Core modules are pure Kotlin in `commonMain` with zero runtime dependencies** besides the
  stdlib (`kmpbr-calendar` may use `kotlinx-datetime`). No `expect`/`actual` unless unavoidable.
- **No network access and no data collection** in core modules. Online lookups belong to a separate,
  opt-in module.
- Behavior must be identical on every target: put tests in `commonTest` so they run everywhere.
- Never rely on platform locale (e.g. for BRL formatting) — implement formatting explicitly.
- Public API needs KDoc and must be Swift-friendly: avoid top-level generics and APIs that only work
  through default arguments.
- **All test data is synthetic**: generated values or examples from official documentation. Never
  commit real CPFs, CNPJs, names, addresses or phone numbers.
- Never commit secrets or signing material (see `.gitignore`); publishing credentials come from CI
  secrets only.

## Git workflow

- Small, focused commits — one logical change per commit, no giant "everything" commits.
- Reference the issue in the commit that completes it (`Closes #N`).
- Keep work scoped to the issue at hand; note unrelated problems as follow-ups instead of fixing them.
- Don't change the repository's git `user.email` / author config.
