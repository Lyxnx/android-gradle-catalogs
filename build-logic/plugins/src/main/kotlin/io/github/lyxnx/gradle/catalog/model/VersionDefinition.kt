package io.github.lyxnx.gradle.catalog.model

sealed interface VersionDefinition {
    data class Simple(val version: String) : VersionDefinition
    data class Reference(val ref: String) : VersionDefinition
    data object Unspecified : VersionDefinition
}
