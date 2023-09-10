package io.github.lyxnx.gradle.android.catalogs.base

import org.gradle.api.Plugin
import org.gradle.api.Project

public abstract class AbstractPlugin : Plugin<Project> {

    public lateinit var project: Project
        private set

    final override fun apply(target: Project) {
        project = target
        target.configure()
    }

    protected abstract fun Project.configure()
}