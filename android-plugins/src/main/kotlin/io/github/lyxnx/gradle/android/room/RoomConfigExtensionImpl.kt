package io.github.lyxnx.gradle.android.room

import io.github.lyxnx.gradle.android.base.WithDefaults
import org.gradle.api.Project
import java.io.File

@Suppress("LeakingThis")
internal abstract class RoomConfigExtensionImpl(project: Project) : RoomConfigExtension,
    WithDefaults<RoomConfigExtensionImpl> {

    init {
        schemaDir.convention(File(project.projectDir, RoomConfigExtension.DEFAULT_DIR_NAME))
            .finalizeValueOnRead()
    }

    override fun setDefaults(defaults: RoomConfigExtensionImpl) {
        schemaDir.convention(defaults.schemaDir)
    }
}