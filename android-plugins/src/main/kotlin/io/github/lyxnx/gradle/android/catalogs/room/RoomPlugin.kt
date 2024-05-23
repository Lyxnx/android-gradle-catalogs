package io.github.lyxnx.gradle.android.catalogs.room

import com.google.devtools.ksp.gradle.KspExtension
import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.catalogs.internal.android
import io.github.lyxnx.gradle.android.catalogs.internal.androidTestImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensureCatalogLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.ksp
import io.github.lyxnx.gradle.android.catalogs.room.RoomPlugin.Companion.DEFAULT_SCHEMA_DIR
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findPlugin
import org.gradle.process.CommandLineArgumentProvider
import java.io.File
import javax.inject.Inject

private const val KSP_PLUGIN = "com.google.devtools.ksp"

/**
 * Configures RoomDB for a project
 *
 * This will apply the `com.google.devtools.ksp` plugin and add the following dependencies:
 * - `room-runtime` and `room-ktx` as `implementation` dependencies
 * - `room-testing` as a `androidTestImplementation` dependency
 *
 * If [roomSchemaDir] is never explicitly called, this will default to [DEFAULT_SCHEMA_DIR]
 */
// TODO support KMP in Room 2.7.0 (https://developer.android.com/jetpack/androidx/releases/room#2.7.0-alpha01) and https://developer.android.com/kotlin/multiplatform/room
public class RoomPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : CatalogsBasePlugin() {

    public companion object {
        /**
         * The default schema directory, relative to the project
         */
        public const val DEFAULT_SCHEMA_DIR: String = "schemas"
    }

    internal var addedSchema: Boolean = false

    override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("KSP", KSP_PLUGIN)
        apply(plugin = KSP_PLUGIN)

        dependencies {
            implementation(ensureCatalogLibrary("androidx.room:room-runtime"))
            implementation(ensureCatalogLibrary("androidx.room:room-ktx"))
            ksp(ensureCatalogLibrary("androidx.room:room-compiler"))
            androidTestImplementation(ensureCatalogLibrary("androidx.room:room-testing"))
        }

        afterEvaluate {
            // Only set the default schema directory if it was never explicitly set
            if (!addedSchema) {
                roomSchemaDir(DEFAULT_SCHEMA_DIR)
            }
        }
    }
}

internal class RoomSchemeArgProvider(
    @get: InputDirectory
    @get: PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File,
) : CommandLineArgumentProvider {
    override fun asArguments(): List<String> = listOf("room.schemaLocation=${schemaDir.path}")
}

/**
 * Registers [path] as a RoomDB schema directory
 *
 * If the directory does not exist, it will be created
 *
 * The directory will also be added to the androidTest assets source directories
 *
 * @param path path to the schema directory. This is relative to the current project directory
 */
public fun Project.roomSchemaDir(path: String) {
    roomSchemaDir(File(path))
}

/**
 * Registers [schemaDir] as a RoomDB schema directory
 *
 * If the directory does not exist, it will be created
 *
 * The directory will also be added to the androidTest assets source directories
 *
 * @param schemaDir the schema directory. This is relative to the current project directory
 */
public fun Project.roomSchemaDir(schemaDir: File) {
    @Suppress("NAME_SHADOWING")
    val schemaDir = File(projectDir, schemaDir.path)

    if (!schemaDir.exists()) {
        schemaDir.mkdirs()
    }

    extensions.configure<KspExtension> {
        arg(RoomSchemeArgProvider(schemaDir))
    }

    android<AndroidCommonExtension> {
        sourceSets {
            findByName("androidTest")?.assets?.srcDir(schemaDir)
        }
    }

    (plugins.findPlugin(RoomPlugin::class) ?: error("Room plugin not applied")).addedSchema = true
}
