plugins {
    id("catalogs")
}

description = "Version catalog for third party libraries"

catalogs {
    // won't be able to find the latest version when releasing a new version
    verificationExcludes += listOf(
        "catalogs-compose-compiler",
        "catalogs-compose-ui",
        "catalogs-room",
        "catalogs-hilt",
        "catalogs-firebase",
        "catalogs-androidplugins"
    )
}
