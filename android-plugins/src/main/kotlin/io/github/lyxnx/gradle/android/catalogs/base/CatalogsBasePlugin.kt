package io.github.lyxnx.gradle.android.catalogs.base

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

public abstract class CatalogsBasePlugin : AbstractPlugin() {

    final override fun Project.configure() {
        check((extensions.findByType<ApplicationExtension>() ?: extensions.findByType<LibraryExtension>()) != null) {
            "Cannot get Android module type (application/library) - make sure the corresponding AGP sub-plugin is applied"
        }

        configureCatalogPlugin()
    }

    protected abstract fun Project.configureCatalogPlugin()
}