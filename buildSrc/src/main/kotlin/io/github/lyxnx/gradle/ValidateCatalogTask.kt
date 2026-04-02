package io.github.lyxnx.gradle

import io.github.lyxnx.gradle.catalog.VersionCatalogParser
import io.github.lyxnx.gradle.catalog.model.Library
import io.github.lyxnx.gradle.catalog.model.Plugin
import io.github.lyxnx.gradle.resolver.DependencyResolver
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.language.base.plugins.LifecycleBasePlugin

abstract class ValidateCatalogTask : DefaultTask() {

    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @get:Input
    abstract val excludes: ListProperty<String>

    init {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        description = "Validates version catalog"
    }

    @TaskAction
    fun validate() {
        val parser = VersionCatalogParser()
        val parsed = parser.parse(inputFile.get().asFile.inputStream())

        val resolver = DependencyResolver(project.logger)
        val result = resolver.resolve(
            project = project,
            versionCatalog = parsed,
            excludedAliases = excludes.getOrElse(emptyList())
        )

        val message = buildString {
            if (!result.excluded.isEmpty()) {
                appendLine("Excluded:")
                appendLine(
                    buildList(
                        libraries = result.excluded.libraries,
                        plugins = result.excluded.plugins,
                        depth = 2
                    )
                )
            }

            if (!result.resolved.isEmpty()) {
                appendLine("Resolved:")
                appendLine(
                    buildList(
                        libraries = result.resolved.libraries,
                        plugins = result.resolved.plugins,
                        depth = 2
                    )
                )
            }

            if (!result.unresolved.isEmpty()) {
                appendLine("Unresolved:")
                appendLine(
                    buildList(
                        libraries = result.unresolved.libraries,
                        plugins = result.unresolved.plugins,
                        depth = 2
                    )
                )
            }
        }.trim()

        if (!result.unresolved.isEmpty()) {
            throw GradleException(message)
        } else {
            logger.lifecycle(message)
        }
    }

    private fun buildList(
        libraries: List<Library>,
        plugins: List<Plugin>,
        depth: Int = 0
    ) = buildString {
        fun withIndent(block: StringBuilder.() -> Unit) {
            repeat(depth) { append(" ") }
            block()
        }

        if (libraries.isEmpty() && plugins.isEmpty()) {
            withIndent {
                appendLine(" - None")
            }
        }

        if (libraries.isNotEmpty()) {
            withIndent {
                appendLine("Libraries:")
            }

            for (lib in libraries) {
                withIndent {
                    appendLine(" - ${lib.module}")
                }
            }
        }

        if (plugins.isNotEmpty()) {
            withIndent {
                appendLine("Plugins:")
            }

            for (pl in plugins) {
                withIndent {
                    appendLine("  - ${pl.id}")
                }
            }
        }
    }

    companion object {
        const val NAME = "validateCatalog"
    }
}
