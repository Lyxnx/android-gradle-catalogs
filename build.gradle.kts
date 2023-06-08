plugins {
    `version-catalog`
    id("net.lyxnx.android.catalog-extensions") version "1.0" apply false
    alias(common.plugins.android.application) apply false
    alias(common.plugins.kotlin.android) apply false
}

subprojects {
    group = "net.lyxnx.android"
    version = "2023.06.08"

    apply(plugin = "net.lyxnx.android.catalog-extensions")
}