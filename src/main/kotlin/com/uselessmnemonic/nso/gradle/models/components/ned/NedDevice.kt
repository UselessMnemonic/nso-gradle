package com.uselessmnemonic.nso.gradle.models.components.ned

import org.gradle.api.provider.Property

/**
 * Describes NED device information.
 */
interface NedDevice {
    /**
     * Free-form string, used for documentation purposes.
     */
    val vendor: Property<String>

    /**
     * The product-specific family of products supported by this NED.
     *
     * In some cases, this could be as generic as 'ios', if the
     * NED works with any ios release.  In other cases it could
     * be as specific as 'ios-11.4' if it works only with ios
     * release 11.4.  And in some cases, it would be some middle
     * ground, e.g., 'ios-11' or 'ios-11+'.
     */
    val productFamily: Property<String>
}
