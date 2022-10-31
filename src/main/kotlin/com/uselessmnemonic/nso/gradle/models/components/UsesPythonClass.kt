package com.uselessmnemonic.nso.gradle.models.components

import org.gradle.api.provider.Provider

/**
 * Describes a component implemented in Python.
 */
interface UsesPythonClass {
    /**
     * The name of the Python class.
     */
    val pythonClassName: Provider<String>
}
