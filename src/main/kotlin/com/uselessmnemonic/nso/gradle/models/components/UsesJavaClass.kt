package com.uselessmnemonic.nso.gradle.models.components

import org.gradle.api.provider.Provider

/**
 * Describes a component implemented in Java.
 */
interface UsesJavaClass {
    /**
     * The name of the Java class.
     */
    val javaClassName: Provider<String>
}
