package io.github.lyxnx.gradle

abstract class CatalogsExtension {

    /**
     * List of plugin or library aliases to exclude from the verification task
     *
     * This can be specified in any format a catalog accepts : _, - or .
     */
    val verificationExcludes = mutableListOf<String>()

    companion object {
        const val NAME = "catalogs"
    }

}