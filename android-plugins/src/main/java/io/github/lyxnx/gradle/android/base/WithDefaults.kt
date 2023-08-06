package io.github.lyxnx.gradle.android.base

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create
import kotlin.reflect.KClass

public fun interface WithDefaults<T> {
    public fun setDefaults(defaults: T)
}

@PublishedApi
internal inline fun <reified T : WithDefaults<T>> ExtensionContainer.createWithDefaults(
    name: String,
    defaults: T?,
    publicType: KClass<in T>? = null,
    constructorArgs: Array<Any>? = null
): T {
    val extension = if (publicType == null) {
        create(name = name, constructionArguments = constructorArgs ?: emptyArray())
    } else {
        create(
            name = name,
            publicType = publicType,
            instanceType = T::class,
            constructionArguments = constructorArgs ?: emptyArray()
        ) as T
    }
    return extension.apply { if (defaults != null) setDefaults(defaults) }
}