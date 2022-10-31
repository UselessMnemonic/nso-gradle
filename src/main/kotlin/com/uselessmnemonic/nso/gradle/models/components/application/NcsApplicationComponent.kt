package com.uselessmnemonic.nso.gradle.models.components.application

import com.uselessmnemonic.nso.gradle.models.components.NcsPackageComponent
import org.gradle.api.provider.Property

/**
 * An application is a generic type with some code.
 */
sealed interface NcsApplicationComponent : NcsPackageComponent {
    /**
     * NCS start phase where the package is instantiated.
     */
    val startPhase: Property<String>
}
