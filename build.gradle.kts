import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.MavenPublishPlugin

plugins {
    `version-catalog`
    id("net.lyxnx.android.catalog-extensions") version "1.0" apply false
    id("com.vanniktech.maven.publish") version "0.25.3" apply false
}

subprojects {
    group = "io.github.lyxnx"
    version = "2023.08.02"

    if (name.startsWith("versions-")) {
        apply(plugin = "net.lyxnx.android.catalog-extensions")
        apply(plugin = "com.vanniktech.maven.publish")

        plugins.withType<MavenPublishPlugin> {
            extensions.configure<MavenPublishBaseExtension> {
                coordinates(this@subprojects.group.toString(), this@subprojects.name, this@subprojects.version.toString())

                publishToMavenCentral(SonatypeHost.Companion.S01, true)
                signAllPublications()

                pom {
                    name.set(this@subprojects.name)
                    description.set(this@subprojects.description)
                    inceptionYear.set("2023")
                    url.set("https://github.com/Lyxnx/android-gradle-catalogs")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("http://www.opensource.org/licenses/mit-license.php")
                        }
                    }
                    developers {
                        developer {
                            id.set("Lyxnx")
                            name.set("Lyxnx")
                            url.set("https://github.com/Lyxnx")
                        }
                    }
                    scm {
                        url.set("https://github.com/Lyxnx/android-gradle-catalogs")
                        connection.set("https://github.com/Lyxnx/android-gradle-catalogs.git")
                        developerConnection.set("scm:git:ssh://git@github.com/Lyxnx/android-gradle-catalogs.git")
                    }
                }
            }
        }
    }
}
