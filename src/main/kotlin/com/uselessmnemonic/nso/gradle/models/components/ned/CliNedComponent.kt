package com.uselessmnemonic.nso.gradle.models.components.ned

import com.uselessmnemonic.nso.gradle.models.components.UsesJavaClass
import org.gradle.api.provider.Property

/**
 * A CLI NED typically consists of YANG models for the device, Java classes
 * derived from these modules, and a Java class responsible for the
 * communication with the device.
 */
interface CliNedComponent : NcsNedComponent, UsesDevice, UsesJavaClass {
    /**
     * The name of the Java class that implements the interface NedCli.
     */
    override val javaClassName: Property<String>
}
