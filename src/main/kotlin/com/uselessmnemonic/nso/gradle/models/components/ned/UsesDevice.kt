package com.uselessmnemonic.nso.gradle.models.components.ned

import org.gradle.api.tasks.Nested

/**
 * Describes a NED component that defines a NED device.
 */
interface UsesDevice {
    /**
     * The NED device.
     */
    @get:Nested
    val device: NedDevice
}
