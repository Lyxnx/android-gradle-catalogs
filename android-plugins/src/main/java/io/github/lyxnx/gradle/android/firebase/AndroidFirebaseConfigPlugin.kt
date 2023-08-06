package io.github.lyxnx.gradle.android.firebase

import io.github.lyxnx.gradle.android.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.base.ensureLibrary
import io.github.lyxnx.gradle.android.base.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

public class AndroidFirebaseConfigPlugin : CatalogsBasePlugin() {

    override fun Project.configure() {
        ensurePlugin("com.google.gms.google-services")

        afterEvaluate {
            val catalog = findCatalog(baseExtension.firebaseCatalogName.get())

            dependencies {
                implementation(platform(catalog.ensureLibrary("bom")))
            }
        }
    }
}
