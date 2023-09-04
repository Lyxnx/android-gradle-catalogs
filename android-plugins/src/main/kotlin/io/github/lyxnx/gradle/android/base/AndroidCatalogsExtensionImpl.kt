package io.github.lyxnx.gradle.android.base

import io.github.lyxnx.gradle.android.base.internal.findByName
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer

@Suppress("LeakingThis")
internal abstract class AndroidCatalogsExtensionImpl : AndroidCatalogsExtension, ExtensionAware,
    WithDefaults<AndroidCatalogsExtensionImpl> {

    init {
        commonCatalogName.convention(COMMON_CATALOG_NAME).finalizeValueOnRead()
        composeCatalogName.convention(COMPOSE_CATALOG_NAME).finalizeValueOnRead()
        androidxCatalogName.convention(ANDROIDX_CATALOG_NAME).finalizeValueOnRead()
        firebaseCatalogName.convention(FIREBASE_CATALOG_NAME).finalizeValueOnRead()
    }

    override fun setDefaults(defaults: AndroidCatalogsExtensionImpl) {
        commonCatalogName.convention(defaults.commonCatalogName)
        composeCatalogName.convention(defaults.composeCatalogName)
        androidxCatalogName.convention(defaults.androidxCatalogName)
        firebaseCatalogName.convention(defaults.firebaseCatalogName)
    }

    companion object {
        const val COMMON_CATALOG_NAME = "common"
        const val COMPOSE_CATALOG_NAME = "compose"
        const val ANDROIDX_CATALOG_NAME = "androidx"
        const val FIREBASE_CATALOG_NAME = "firebase"
    }

}

internal val ExtensionContainer.androidGradleCatalogs: AndroidCatalogsExtensionImpl?
    get() = findByName<AndroidCatalogsExtensionImpl>(AndroidCatalogsExtension.NAME)