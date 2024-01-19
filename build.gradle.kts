tasks.register("publishPlugins") {
    group = "Publishing"
    description = "Publishes all Android plugins to Maven Central"

    dependsOn(":android-plugins:publish")
}

tasks.register("publishCatalogs") {
    group = "Publishing"
    description = "Publishes all catalogs to Maven Central"

    subprojects.filter { it.name.startsWith("versions-") }.forEach {
        dependsOn(it.tasks["publish"])
    }
}