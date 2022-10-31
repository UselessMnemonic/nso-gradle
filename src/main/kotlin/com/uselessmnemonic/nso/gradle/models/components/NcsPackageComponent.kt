package com.uselessmnemonic.nso.gradle.models.components

import org.gradle.api.Named
import org.gradle.api.provider.Property

/**
 * Describes a package component.
 */
interface NcsPackageComponent : Named {
    /**
     * A unique name, used to identify the component within the package.
     */
    override fun getName(): String

    /**
     * Free-form text describing the component.
     */
    val description: Property<String>

    /**
     * Identifies the component for Smart Licensing.
     */
    val entitlementTag: Property<String>
}
