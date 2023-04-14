plugins {
    `version-catalog`
    id("net.lyxnx.android.catalog-extensions") version "1.0" apply false
}

subprojects {
    apply(plugin = "version-catalog")
    apply(plugin = "net.lyxnx.android.catalog-extensions")

    group = "net.lyxnx.android"
    version = "2023.04.05"

    catalog {
        versionCatalog {
            from(files("libs.versions.toml"))
        }
    }

}
