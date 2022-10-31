package com.uselessmnemonic.nso.gradle.models.components.application

import com.uselessmnemonic.nso.gradle.models.components.UsesJavaClass
import org.gradle.api.provider.Property

/**
 * Describes a Java application component.
 */
interface JavaApplicationComponent : NcsApplicationComponent, UsesJavaClass {
    /**
     * The name of the Java class that implements `com.tailf.ncs.ApplicationComponent`
     */
    override var javaClassName: Property<String>
}
