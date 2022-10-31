package com.uselessmnemonic.nso.gradle.models.packages

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Nested
import javax.inject.Inject

/**
 * Describes the root ncs-package element of the project metadata.
 *
 * Its configuration will be used to generate `project-meta-data.xml`.
 */
abstract class NcsPackage @Inject internal constructor(objects: ObjectFactory) {

    /**
     * A unique name used to identify the package.
     * By default, it is this project's name.
     */
    abstract val name: Property<String>

    /**
     * The version of the package, as a sequence of numbers and words, seperated by
     * '.' or '-'.
     *
     * When `ncs` compares a version string, it parses the string into the sequence
     * of numbers and words, and compares each component individually.
     *
     * By default, it is this project's version.
     */
    abstract val packageVersion: Property<String>

    /**
     * Free-form text describing the package.
     */
    abstract val description: Property<String>

    /**
     * Minimum `ncs` versions, one per NSO major, required by this package.
     */
    abstract val ncsMinVersions: SetProperty<String>

    /**
     * Python specific configuration.
     */
    @get:Nested
    abstract val pythonOptions: NcsPythonOptions

    /**
     * Configures the Python options for this package.
     * @param action The configuration [action][Action].
     */
    fun pythonOptions(action: Action<NcsPythonOptions>) = action.execute(pythonOptions)

    /**
     * The list of ned-ids supported by this package.
     */
    abstract val supportedNedIds: SetProperty<String>

    /**
     * A list of packages required by this package.
     *
     * Each key names the package, and its value the minimum required version.
     */
    abstract val requiredPackages: MapProperty<String, String>

    /**
     * A list of package components, like NED and application classes.
     *
     * A project must have at least one component.
     */
    val components: NcsComponentContainer = objects.newInstance(NcsComponentContainer::class.java)

    /**
     * Configures the package components.
     * @param action The configuration [action][Action].
     */
    fun components(action: Action<NcsComponentContainer>) = action.execute(components)
}
