package io.github.lyxnx.gradle.catalog.model

data class Library(
    val module: String,
    override val version: VersionDefinition,
) : Versioned {
    constructor(
        group: String,
        name: String,
        version: VersionDefinition
    ) : this("$group:$name", version)

    val group by lazy {
        module.split(":").dropLast(1).joinToString()
    }
    val name by lazy {
        module.split(":").last()
    }
}
