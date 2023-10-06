plugins {
    id("catalogs")
}

description = "Version catalog for third party libraries"

catalogs {
    // won't be able to find the latest version when releasing a new version
    verificationExcludes += listOf(
        "catalogs-compose-compiler-config",
        "catalogs-config",
        "catalogs-compose-config",
        "catalogs-room-config",
        "catalogs-hilt-config",
        "catalogs-firebase-config",
        "catalogs-androidplugins"
    )
}