package io.github.lyxnx.gradle

import groovy.lang.MissingPropertyException
import org.gradle.api.Project

internal fun Project.propertyOrNull(name: String) = try {
    property(name)
} catch (_: MissingPropertyException) {
    null
}