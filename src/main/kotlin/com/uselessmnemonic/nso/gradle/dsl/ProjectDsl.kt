package com.uselessmnemonic.nso.gradle.dsl

import com.uselessmnemonic.nso.gradle.plugin.NsoProjectExtension
import org.gradle.api.Action
import org.gradle.api.Project

/**
 * The [nso][NsoProjectExtension] extension.
 */
val Project.nso get() =
    extensions.getByName("nso") as NsoProjectExtension

/**
 * Configures the [nso extension][NsoProjectExtension].
 * @param configure The configuration [action][Action].
 */
fun Project.nso(configure: Action<NsoProjectExtension>) =
    extensions.configure("nso", configure)
