# Requirements — kmp-br

## 1. Goal
Provide a single, well-tested Kotlin Multiplatform library for Brazil-specific validation, formatting and parsing that behaves identically on every supported target.

## 2. Stakeholders
- **App developers** (KMP, Android, iOS via Swift, JVM backend) — primary users.
- **Maintainer** — needs low-cost releases and automated verification.

## 3. Functional requirements

| ID | Requirement |
|---|---|
| FR-01 | Validate, format and strip CPF. |
| FR-02 | Validate, format and strip CNPJ in both numeric and alphanumeric formats, using the Receita Federal mod-11 algorithm with ASCII−48 character values. |
| FR-03 | Generate syntactically valid **test** CPFs/CNPJs (clearly documented as test-only). |
| FR-04 | Validate CNH, título de eleitor, PIS/NIS and RENAVAM. |
| FR-05 | Format and parse BRL amounts (`R$ 1.234,56`) without relying on platform locale. |
| FR-06 | Convert integers and BRL amounts to Portuguese words, with correct gender and plural agreement. |
| FR-07 | List national holidays for any year, including Easter-based dates, distinguishing holidays from "ponto facultativo". |
| FR-08 | Business-day arithmetic using a pluggable holiday provider. |
| FR-09 | Validate DDDs, classify mobile vs. landline, format phones, convert to E.164. |
| FR-10 | Validate old and Mercosul license plates and convert between them. |
| FR-11 | Validate/format CEP; expose UFs and IBGE municipality codes. |
| FR-12 | Normalize free-text addresses (abbreviations, "s/n", highways, complements, casing) and parse them into components. |
| FR-13 | Provide Compose Multiplatform input masks for the formats above (optional module). |

## 4. Non-functional requirements

| ID | Requirement |
|---|---|
| NFR-01 | Core modules have **no runtime dependencies** other than the Kotlin stdlib (calendar may use `kotlinx-datetime`). |
| NFR-02 | Identical results on all targets — verified by running the same `commonTest` suite on each. |
| NFR-03 | ≥ 90% line coverage on core modules; property-based tests for check-digit algorithms. |
| NFR-04 | Public API documented with KDoc; binary compatibility checked in CI. |
| NFR-05 | Swift-friendly API (no top-level generics or default-arg-only APIs that break in Obj-C export). |
| NFR-06 | No network access in core modules; no collection or storage of any user data. |
| NFR-07 | Each module adds < 100 KB to an Android release build. |

## 5. Out of scope
- Looking up real people's or companies' registration data (e.g. "consultar CPF").
- Payment processing, Pix transactions, or anything that handles financial records.
- Bundling third-party datasets whose license doesn't allow redistribution.

## 6. Data sources
- Check-digit algorithms: public Receita Federal / Serpro technical documentation.
- IBGE municipality codes: public IBGE datasets (license to be confirmed and noted in `NOTICE` before bundling).
- Holidays: Brazilian federal law (Lei 662/1949 and amendments).

## 7. Acceptance criteria for v1.0
- All FR-01…FR-12 implemented with tests passing on Android, JVM, iOS, JS and Wasm in CI.
- Published to Maven Central; Swift Package available.
- README examples compile (verified by a docs test).
