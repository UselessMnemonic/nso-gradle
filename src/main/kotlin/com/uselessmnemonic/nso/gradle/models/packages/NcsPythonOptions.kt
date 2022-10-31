package com.uselessmnemonic.nso.gradle.models.packages

import org.gradle.api.provider.Property

/**
 * Describes python specific package configuration.
 */
interface NcsPythonOptions {
    /**
     * A Python package will per default run in a separate Python VM.
     * This Python VM will get a symbolic name (for reference from within NCS) which per default is the package name.
     * However, by setting vmName, its value will be used as the symbolic name for the Python VM started. This will
     * cause packages with the same vmName to run in the same Python VM.
     */
    val vmName: Property<String>
}
