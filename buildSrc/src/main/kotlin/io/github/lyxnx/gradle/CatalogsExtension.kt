package io.github.lyxnx.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.listProperty

abstract class CatalogsExtension(objectFactory: ObjectFactory) {

    /**
     * List of plugin or library aliases to exclude from the verification task
     *
     * This can be specified in any format a catalog accepts : _, - or .
     */
    val verificationExcludes = objectFactory.listProperty<String>()

    companion object {
        const val NAME = "catalogs"
    }

}
