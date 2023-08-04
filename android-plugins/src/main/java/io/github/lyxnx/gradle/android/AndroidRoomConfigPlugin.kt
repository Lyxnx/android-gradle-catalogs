package io.github.lyxnx.gradle.android

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.process.CommandLineArgumentProvider
import java.io.File

class AndroidRoomConfigPlugin : BasePlugin<RoomConfigExtension>(RoomConfigExtension::class, RoomConfigExtension.NAME) {

    override fun Project.configure() {
        pluginManager.apply("com.google.devtools.ksp")

        afterEvaluate {
            extension.androidxCatalog.finalizeValueOnRead()
            extension.schemaDir.finalizeValueOnRead()

            val catalog = findCatalog(extension.androidxCatalog.get())
            val schemaDir = extension.schemaDir.get()

            extensions.configure<KspExtension> {
                arg(RoomSchemeArgProvider(schemaDir))
            }

            dependencies {
                implementation(catalog.ensureLibrary("room.runtime"))
                implementation(catalog.ensureLibrary("room.ktx"))
                ksp(catalog.ensureLibrary("room.compiler"))
                androidTestImplementation(catalog.ensureLibrary("room.testing"))
            }
        }
    }

    class RoomSchemeArgProvider(
        @get: InputDirectory
        @get: PathSensitive(PathSensitivity.RELATIVE)
        val schemaDir: File
    ) : CommandLineArgumentProvider {
        override fun asArguments(): List<String> = listOf("room.schemaLocation=${schemaDir.path}")
    }
}

abstract class RoomConfigExtension(project: Project) : BaseExtension {

    val androidxCatalog: Property<String> = project.objects.property(String::class.java)
        .convention(DEFAULT_CATALOG_NAME)

    val schemaDir: Property<File> = project.objects.property(File::class.java)
        .convention(File(project.projectDir, DEFAULT_SCHEMA_DIR_NAME))

    companion object {
        const val NAME = "roomConfig"
        const val DEFAULT_CATALOG_NAME = "androidx"
        const val DEFAULT_SCHEMA_DIR_NAME = "schemas"
    }

}
