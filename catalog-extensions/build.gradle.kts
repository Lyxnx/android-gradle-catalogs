plugins {
    `kotlin-dsl`
}

group = "net.lyxnx.android.gradle"
version = "1.0"

gradlePlugin {
    plugins {
        create("catalogExtensions") {
            id = "net.lyxnx.android.catalog-extensions"
            implementationClass = "net.lyxnx.android.gradle.CatalogExtensionsPlugin"
        }
    }
}

repositories {
    mavenCentral()
}