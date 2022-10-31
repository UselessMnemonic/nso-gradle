package com.uselessmnemonic.nso.gradle.models.components.application

import com.uselessmnemonic.nso.gradle.models.components.UsesPythonClass
import org.gradle.api.provider.Property

/**
 * Describes a Python application component.
 */
interface PythonApplicationComponent : NcsApplicationComponent, UsesPythonClass {
    /**
     * The name of the Python class that will be started by `ncs`.
     *
     * An empty string here indicates a Python Library, i.e., no code will be invoked by NCS.
     * However, other packages may depend on this package, thus getting the PYTHONPATH setup
     * to point to this package.
     */
    override val pythonClassName: Property<String>
}
