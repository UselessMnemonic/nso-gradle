package com.uselessmnemonic.nso.gradle.models.components.upgrade

import com.uselessmnemonic.nso.gradle.models.components.UsesPythonClass
import org.gradle.api.provider.Property

/**
 * Describes a Python upgrade component.
 */
interface PythonUpgradeComponent : NcsUpgradeComponent, UsesPythonClass {
    /**
     * Name of the Python class that implements an upgrade method
     * which will migrate data.
     */
    override val pythonClassName: Property<String>
}
