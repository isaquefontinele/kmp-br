# Roadmap

Estimates are rough and include tests and docs. Total planned work before 1.0: **~70–90 hours**.

## Milestone 0 — Project foundation (~8h)
- [ ] Gradle multi-module setup with version catalog and convention plugins
- [ ] Targets: Android, JVM, iOS, macOS, JS, Wasm
- [ ] GitHub Actions: build + test on Linux and macOS runners
- [ ] Detekt / ktlint, binary-compatibility validator
- [ ] Dokka API docs published to GitHub Pages

## Milestone 1 — Documents (~16h)
- [ ] CPF: validate, format, strip, generate (test values)
- [ ] CNPJ numeric: validate, format, strip, generate
- [ ] CNPJ **alphanumeric**: ASCII−48 mod-11 check digits, letter rules, format/strip
- [ ] CNH, título de eleitor, PIS/NIS, RENAVAM validators
- [ ] Property-based tests (generated values always validate; single-char mutations fail)

## Milestone 2 — Money and words (~12h)
- [ ] `Brl.format` / `Brl.parse` with locale-independent behavior on every target
- [ ] Numbers in words (cardinals up to trillions, gender agreement: "duas mil")
- [ ] Amounts in words ("um real", "dois reais e um centavo", "um milhão de reais")
- [ ] Ordinals ("primeiro", "vigésimo terceiro")

## Milestone 3 — Calendar (~10h)
- [ ] Fixed national holidays (incl. 20 Nov — Consciência Negra)
- [ ] Easter computus; Carnaval, Sexta-feira Santa, Corpus Christi
- [ ] Holiday kinds: national holiday vs. ponto facultativo
- [ ] Business-day arithmetic, ranges, "next business day"
- [ ] Extension point for state/municipal holiday providers

## Milestone 4 — Phones and plates (~8h)
- [ ] Valid DDD table, mobile vs. landline detection
- [ ] Formatting and E.164 conversion
- [ ] License plates: old and Mercosul validation, conversion old ↔ Mercosul

## Milestone 5 — Address (~16h)
- [ ] CEP validation/format; UF enum with names and regions
- [ ] IBGE municipality codes (generated from public IBGE data at build time)
- [ ] Address normalizer: street-type abbreviations, "s/n", highways (BR-101, SC-401), apartment/block complements, accents and casing
- [ ] Address parser: split a free-text line into street type, name, number, complement
- [ ] Golden-file test corpus of synthetic addresses

## Milestone 6 — Compose input masks (~8h)
- [ ] `VisualTransformation`s for CPF, CNPJ, CEP, phone, money
- [ ] Sample Compose Multiplatform app (Android + iOS + desktop)

## Milestone 7 — Release 1.0 (~6h)
- [ ] Maven Central publishing with signing via CI secrets (never in repo)
- [ ] XCFramework + Swift Package for iOS consumers
- [ ] Docs site with recipes; CHANGELOG; semantic versioning policy

## Later
- Optional `kmpbr-cep-online` module (ViaCEP / BrasilAPI lookup via Ktor), opt-in
- Boleto line ("linha digitável") **format** validation
- State and municipal holiday datasets
