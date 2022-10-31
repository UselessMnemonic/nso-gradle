package com.uselessmnemonic.nso.gradle.plugin

import com.uselessmnemonic.nso.gradle.models.packages.NcsPackage
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import javax.inject.Inject

/**
 * Container for configurable properties of the plugin.
 */
abstract class NsoProjectExtension @Inject constructor(objects: ObjectFactory) {
    /**
     * Tracks [yang source sets][YangSourceSet].
     */
    val sourceSets: YangSourceSetContainer = objects.newInstance(YangSourceSetContainer::class.java)

    /**
     * Configures the yang source sets.
     */
    fun sourceSets(action: Action<YangSourceSetContainer>) = action.execute(sourceSets)

    /**
     * The [NcsPackage] package configuration.
     */
    @get:Nested
    abstract val ncsPackage: NcsPackage

    /**
     * Configures the ncs package.
     */
    fun ncsPackage(action: Action<NcsPackage>) = action.execute(ncsPackage)

    /**
     * The location of the target NSO version. By default, it is pulled from the `NCS_DIR` environment variable.
     */
    abstract val ncsDir: Property<String>
}
