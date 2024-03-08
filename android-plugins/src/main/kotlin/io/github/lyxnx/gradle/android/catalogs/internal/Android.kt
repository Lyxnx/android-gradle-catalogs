package io.github.lyxnx.gradle.android.catalogs.internal

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project

internal typealias AndroidCommonExtension = CommonExtension<*, *, *, *, *, *>

internal fun <T : AndroidCommonExtension> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}

@JvmName("androidComponentsCommon")
internal fun Project.androidComponents(configure: AndroidComponentsExtension<*, *, *>.() -> Unit) {
    androidComponents<AndroidComponentsExtension<*, *, *>>(configure)
}

internal fun <T : AndroidComponentsExtension<*, *, *>> Project.androidComponents(configure: T.() -> Unit) {
    extensions.configure("androidComponents", configure)
}