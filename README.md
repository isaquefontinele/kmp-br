# kmp-br

**Brazilian utilities for Kotlin Multiplatform** — validate, format and parse Brazilian documents, addresses, phones, money, dates and holidays with one library that runs on Android, iOS, JVM and the web.

> 🇧🇷 *Utilitários brasileiros para Kotlin Multiplatform: CPF, CNPJ (inclusive alfanumérico), CEP, telefone, placas, moeda, datas, feriados, dias úteis, valores por extenso e normalização de endereços.*

> **Status:** early development. The API is not stable yet. See [ROADMAP.md](ROADMAP.md).

## Why

Every Brazilian app re-implements the same helpers — CPF/CNPJ checks, input masks, phone formatting, "R$ 1.234,56", holidays — usually copy-pasted per platform and often wrong in edge cases. Existing libraries are mostly JVM-only or JavaScript-only. Kotlin Multiplatform apps have no well-maintained, tested option that works the same everywhere.

Since July 2026 the Receita Federal issues **alphanumeric CNPJs** (e.g. `12.ABC.345/01DE-35`). Most validators in the wild still reject them. `kmp-br` supports both formats from day one.

## Who it's for

- Teams building Kotlin Multiplatform / Compose Multiplatform apps for Brazilian users.
- Android and backend (JVM) developers who want a small, dependency-free toolkit.
- iOS developers consuming a KMP shared module from Swift.

## Features (planned for v1.0)

| Module | What it does |
|---|---|
| `kmpbr-documents` | CPF, CNPJ (numeric **and alphanumeric**), CNH, título de eleitor, PIS/NIS, RENAVAM — validate, format, strip, generate test values |
| `kmpbr-address` | CEP format/validation, UF list, IBGE municipality codes, **address normalization** ("R." → "Rua", "Av." → "Avenida", "s/n", "Rod. SC-401", complements) |
| `kmpbr-phone` | Landline/mobile detection, valid DDDs, formatting `(48) 99999-9999`, E.164 conversion |
| `kmpbr-money` | `BRL` formatting and parsing (`R$ 1.234,56`), **numbers and amounts in words** ("mil duzentos e trinta e quatro reais e cinquenta e seis centavos") |
| `kmpbr-calendar` | National holidays (fixed + Easter-based), optional points (ponto facultativo), business-day math (`addBusinessDays`, `isBusinessDay`) |
| `kmpbr-vehicle` | License plates: old format (`ABC-1234`) and Mercosul (`ABC1D23`), conversion between them |
| `kmpbr-compose` *(optional)* | `VisualTransformation`s / input masks for Compose Multiplatform text fields |

Design principles:

- **Pure Kotlin `commonMain`**, zero runtime dependencies in core modules.
- **No network, no data collection.** The library only validates and formats values the host app already has. Optional online lookups (e.g. CEP → address) are a separate, opt-in module.
- Pick only the modules you need.

## Example

```kotlin
import br.kmp.documents.Cnpj
import br.kmp.money.Brl
import br.kmp.calendar.BusinessDays

Cnpj.isValid("12.ABC.345/01DE-35")      // true  (alphanumeric)
Cnpj.format("12345678000195")           // "12.345.678/0001-95"
Brl.format(1234.56)                     // "R$ 1.234,56"
Brl.inWords(1234.56)                    // "mil duzentos e trinta e quatro reais e cinquenta e seis centavos"
BusinessDays.add(LocalDate(2026, 12, 23), 3) // skips Christmas and the weekend
```

From Swift (via the shared framework):

```swift
import KmpBr

let ok = Cnpj.shared.isValid(value: "12.ABC.345/01DE-35")
```

*(API sketch — final names may change before 1.0.)*

## Built with

- Kotlin 2.x, Kotlin Multiplatform
- Targets: Android, JVM, iOS (arm64, simulatorArm64), macOS, JS and Wasm
- `kotlinx-datetime` (calendar module only)
- Tests: `kotlin.test` + property-based tests with Kotest
- Build: Gradle (Kotlin DSL) with version catalogs
- Publishing: Maven Central; Swift Package via XCFramework
- CI: GitHub Actions (all targets), Dokka for API docs

## Getting started

> Not published yet. Once the first release is out:

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation("io.github.isaquefontinele.kmpbr:kmpbr-documents:<version>")
}
```

### Building from source

Requirements: JDK 17+, Xcode 16+ (for Apple targets, macOS only).

```bash
git clone https://github.com/isaquefontinele/kmp-br.git
cd kmp-br
./gradlew check          # runs tests on all available targets
./gradlew :kmpbr-documents:jvmTest
```

## Test data

All CPFs, CNPJs and other identifiers used in tests and examples are **generated or taken from official documentation examples**. The repository never contains real people's or companies' data.

## Contributing

Issues and PRs are welcome. Please open an issue before large changes. See [ROADMAP.md](ROADMAP.md) for planned work and [docs/REQUIREMENTS.md](docs/REQUIREMENTS.md) for the detailed scope.

## License

[MIT](LICENSE) © 2026 Isaque Sales Domingos Fontinele
