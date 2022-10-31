package com.uselessmnemonic.nso.gradle.plugin

import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

/**
 * Describes Yang source sets inside the project.
 */
abstract class YangSourceSet @Inject constructor(private val _name: String, objects: ObjectFactory) : Named {
    /**
     * The name of this source set.
     */
    override fun getName(): String = _name

    /**
     * Contains the directories for this source set.
     */
    val yang: SourceDirectorySet = objects.sourceDirectorySet("yang", "yang")

    /**
     * Configures the `yang` source set.
     */
    fun yang(action: Action<SourceDirectorySet>) = action.execute(yang)
}
