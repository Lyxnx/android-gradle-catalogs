package io.github.lyxnx.gradle.android.catalogs.room

import com.google.devtools.ksp.gradle.KspExtension
import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.androidxCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.catalogs.internal.KSP_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.android
import io.github.lyxnx.gradle.android.catalogs.internal.androidTestImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.ksp
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.process.CommandLineArgumentProvider
import java.io.File
import javax.inject.Inject

public class AndroidRoomConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("KSP", KSP_PLUGIN)
        apply(plugin = KSP_PLUGIN)

        dependencies {
            val catalog = androidxCatalog

            implementation(catalog.ensureLibrary("room.runtime"))
            implementation(catalog.ensureLibrary("room.ktx"))
            ksp(catalog.ensureLibrary("room.compiler"))
            androidTestImplementation(catalog.ensureLibrary("room.testing"))
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
            getByName("androidTest").assets.srcDir(schemaDir)
        }
    }
}