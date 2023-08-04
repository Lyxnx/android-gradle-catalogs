package io.github.lyxnx.gradle.android

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConfigPlugin : BasePlugin<HiltConfigExtension>(HiltConfigExtension::class, HiltConfigExtension.NAME) {

    override fun Project.configure() {
        with(pluginManager) {
            apply("dagger.hilt.android.plugin")
            apply("org.jetbrains.kotlin.kapt")
        }

        afterEvaluate {
            extension.commonCatalog.finalizeValueOnRead()

            val catalog = findCatalog(extension.commonCatalog.get())

            dependencies {
                implementation(catalog.ensureLibrary("hilt"))
                add("kapt", catalog.ensureLibrary("hilt.compiler"))
            }
        }
    }
}

abstract class HiltConfigExtension(project: Project) : BaseExtension {

    val commonCatalog: Property<String> = project.objects.property(String::class.java)
        .convention(DEFAULT_CATALOG_NAME)

    companion object {
        const val NAME = "hiltConfig"
        const val DEFAULT_CATALOG_NAME = "common"
    }
}
