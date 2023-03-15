plugins {
    `version-catalog`
    `maven-publish`
    id("net.lyxnx.android.catalog-extensions") version "1.0" apply false
}

subprojects {
    apply(plugin = "version-catalog")
    apply(plugin = "maven-publish")
    apply(plugin = "net.lyxnx.android.catalog-extensions")

    group = "net.lyxnx.android"
    version = "2023.03.15"

    catalog {
        versionCatalog {
            from(files("libs.versions.toml"))
        }
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Lyxnx/android-gradle-catalogs")
                credentials {
                    username = project.findProperty("gpr.username") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.token") as String? ?: System.getenv("TOKEN")
                }
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                from(components["versionCatalog"])
            }
        }
    }


}
