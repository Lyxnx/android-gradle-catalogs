plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
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
    implementation(shared.android.gradleplugin)
}
