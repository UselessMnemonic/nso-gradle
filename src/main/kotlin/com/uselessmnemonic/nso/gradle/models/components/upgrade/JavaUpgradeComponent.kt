package com.uselessmnemonic.nso.gradle.models.components.upgrade

import com.uselessmnemonic.nso.gradle.models.components.UsesJavaClass
import org.gradle.api.provider.Property

/**
 * Describes a Java upgrade component.
 */
interface JavaUpgradeComponent : NcsUpgradeComponent, UsesJavaClass {
    /**
     * The name of the Java class that implements a main method
     * which will migrate data.
     */
    override val javaClassName: Property<String>
}
