package com.uselessmnemonic.nso.gradle.models.components.ned

import com.uselessmnemonic.nso.gradle.models.components.NcsPackageComponent
import org.gradle.api.provider.Property

/**
 * The base class for all NED components of the `ncs-package`
 */
sealed interface NcsNedComponent : NcsPackageComponent {
    /**
     * A unique identity that identifies the NED.
     */
    val nedId: Property<String>
}
