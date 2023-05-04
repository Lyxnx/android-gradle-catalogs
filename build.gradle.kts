plugins {
    `version-catalog`
    id("net.lyxnx.android.catalog-extensions") version "1.0" apply false
    alias(common.plugins.android.application) apply false
    alias(common.plugins.kotlin.android) apply false
}
