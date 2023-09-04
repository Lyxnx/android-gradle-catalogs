package io.github.lyxnx.gradle.android.room

import com.google.devtools.ksp.gradle.KspExtension
import io.github.lyxnx.gradle.android.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.base.androidTestImplementation
import io.github.lyxnx.gradle.android.base.ensureLibrary
import io.github.lyxnx.gradle.android.base.implementation
import io.github.lyxnx.gradle.android.base.ksp
import org.gradle.api.Project
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.process.CommandLineArgumentProvider
import java.io.File

public class AndroidRoomConfigPlugin : CatalogsBasePlugin() {

    private lateinit var roomConfig: RoomConfigExtensionImpl

    override fun Project.configure() {
        ensurePlugin("com.google.devtools.ksp")

        plugins.apply("com.google.devtools.ksp")

        roomConfig = createExtension(RoomConfigExtension.NAME, RoomConfigExtension::class, arrayOf(project))

        afterEvaluate {
            val catalog = findCatalog(baseExtension.androidxCatalogName.get())
            val schemaDir = roomConfig.schemaDir.get()

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

    public class RoomSchemeArgProvider(
        @get: InputDirectory
        @get: PathSensitive(PathSensitivity.RELATIVE)
        public val schemaDir: File
    ) : CommandLineArgumentProvider {
        override fun asArguments(): List<String> = listOf("room.schemaLocation=${schemaDir.path}")
    }
}
