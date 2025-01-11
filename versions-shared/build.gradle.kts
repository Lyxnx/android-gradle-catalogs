plugins {
    id("catalogs")
}

description = "Version catalog with base dependencies such as gradle plugins"

catalogs {
    // won't be able to find the latest version when releasing a new version
    verificationExcludes += listOf(
        "catalogs-compose-compiler",
        "catalogs-compose-ui",
        "catalogs-room",
        "catalogs-androidplugins",
        "landscapist"
    )
}
