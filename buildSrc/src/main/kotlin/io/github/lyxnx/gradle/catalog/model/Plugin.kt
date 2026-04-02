package io.github.lyxnx.gradle.catalog.model

data class Plugin(
    val id: String,
    override val version: VersionDefinition
) : Versioned
