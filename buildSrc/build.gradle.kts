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
    implementation(shared.kotlin.gradleplugin)
    implementation(shared.vanniktech.publish.gradleplugin)
}
