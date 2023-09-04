package io.github.lyxnx.gradle.android.base.internal

import org.gradle.api.plugins.ExtensionContainer

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
@PublishedApi
internal inline fun <reified T : Any> ExtensionContainer.findByName(name: String): T? {
    return findByName(name)?.let {
        it as? T ?: error("Extension $name cannot be cast to ${T::class.qualifiedName} within container $this")
    }
}