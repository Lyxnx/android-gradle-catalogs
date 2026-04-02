plugins {
    alias(shared.plugins.kotlin.jvm) apply false
    alias(shared.plugins.vanniktech.publish) apply false
    alias(shared.plugins.android.library) apply false
}

tasks.register("publishPlugins") {
    group = "Publishing"
    description = "Publishes all Android plugins to Maven Central"

    dependsOn(":android-plugins:publish")
}

tasks.register("publishCatalogs") {
    group = "Publishing"
    description = "Publishes all catalogs to Maven Central"

    subprojects.filter { it.name.startsWith("versions-") || it.name == "settings-plugin" }.forEach {
        dependsOn(it.tasks["publish"])
    }
}
