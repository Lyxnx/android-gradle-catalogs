package io.github.lyxnx.gradle.android.base

import org.gradle.api.provider.Property

/**
 * The base namespace extension for all android catalog plugins' extensions
 */
public interface AndroidCatalogsExtension {

    /**
     * The user defined name of the versions-common catalog.
     *
     * Not required to be set unless using a plugin that requires the use of this catalog
     */
    public val commonCatalogName: Property<String>

    /**
     * The user defined name of the versions-compose catalog.
     *
     * Not required to be set unless using a plugin that requires the use of this catalog
     */
    public val composeCatalogName: Property<String>

    /**
     * The user defined name of the versions-androidx catalog.
     *
     * Not required to be set unless using a plugin that requires the use of this catalog
     */
    public val androidxCatalogName: Property<String>

    /**
     * The user defined name of the versions-firebase catalog.
     *
     * Not required to be set unless using a plugin that requires the use of this catalog
     */
    public val firebaseCatalogName: Property<String>

    public companion object {
        /**
         * Name of the main catalogs plugin extension
         */
        public const val NAME: String = "androidCatalogPlugins"
    }

}