package io.github.lyxnx.gradle

/*
Copied and converted to kotlin from org.gradle.api.internal.catalog.AliasNormalizer
as that is package-private and cannot be used here
 */

/*
Catalogs accept their keys represented with _, . or - which is parsed by gradle as .
Eg some-library, some.library, or some_library are all valid and will parse to some.library
 */
private val SEPARATOR = "[_.-]".toRegex()

fun String?.normalizeAlias(): String? {
    if (isNullOrBlank()) {
        return null
    }

    return SEPARATOR.replace(this, ".")
}