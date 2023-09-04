package io.github.lyxnx.gradle.android.room

import org.gradle.api.provider.Property
import java.io.File

/**
 * Configures RoomDB values for the room config plugin
 */
public interface RoomConfigExtension {

    /**
     * The directory used to store database schemas
     *
     * Default: `schemas`
     *
     * Note this is relative to the current project
     */
    public val schemaDir: Property<File>

    public companion object {
        public const val NAME: String = "roomConfig"

        internal const val DEFAULT_DIR_NAME = "schemas"
    }
}
