import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.binaryen.BinaryenEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.binaryen.BinaryenPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.yarn.WasmYarnPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.yarn.WasmYarnRootEnvSpec

/**
 * Stops the Kotlin plugin from adding Node.js, Yarn and Binaryen download repositories to the
 * project. They are declared once in `settings.gradle.kts` instead, which keeps
 * `RepositoriesMode.FAIL_ON_PROJECT_REPOS` working.
 */
@OptIn(ExperimentalWasmDsl::class)
internal fun Project.useSettingsRepositoriesForJsTooling() {
    plugins.withType<NodeJsPlugin> { the<NodeJsEnvSpec>().downloadBaseUrl.set(null as String?) }
    plugins.withType<WasmNodeJsPlugin> { the<WasmNodeJsEnvSpec>().downloadBaseUrl.set(null as String?) }
    plugins.withType<BinaryenPlugin> { the<BinaryenEnvSpec>().downloadBaseUrl.set(null as String?) }
    plugins.withType<YarnPlugin> { the<YarnRootEnvSpec>().downloadBaseUrl.set(null as String?) }
    plugins.withType<WasmYarnPlugin> { the<WasmYarnRootEnvSpec>().downloadBaseUrl.set(null as String?) }
}
