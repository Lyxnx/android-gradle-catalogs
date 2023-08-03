plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("catalogs") {
            id = "catalogs"
            implementationClass = "io.github.lyxnx.gradle.CatalogsPlugin"
        }
    }
}

dependencies {
    implementation(common.vanniktech.publish.plugin)
}
