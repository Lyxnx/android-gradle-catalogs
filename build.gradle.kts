plugins {
    `version-catalog`
    `maven-publish`
}

subprojects {
    apply(plugin = "version-catalog")
    apply(plugin = "maven-publish")

    group = "net.lyxnx.android"
    version = "2023.03.01"

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