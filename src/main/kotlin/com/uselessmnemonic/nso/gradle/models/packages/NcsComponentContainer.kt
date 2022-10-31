package com.uselessmnemonic.nso.gradle.models.packages

import com.uselessmnemonic.nso.gradle.models.components.NcsPackageComponent
import com.uselessmnemonic.nso.gradle.models.components.application.JavaApplicationComponent
import com.uselessmnemonic.nso.gradle.models.components.application.PythonApplicationComponent
import com.uselessmnemonic.nso.gradle.models.components.callback.JavaCallbackComponent
import com.uselessmnemonic.nso.gradle.models.components.ned.CliNedComponent
import com.uselessmnemonic.nso.gradle.models.components.ned.GenericNedComponent
import com.uselessmnemonic.nso.gradle.models.components.ned.NetconfNedComponent
import com.uselessmnemonic.nso.gradle.models.components.ned.SnmpNedComponent
import com.uselessmnemonic.nso.gradle.models.components.upgrade.JavaUpgradeComponent
import com.uselessmnemonic.nso.gradle.models.components.upgrade.PythonUpgradeComponent
import org.gradle.api.Action
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

/**
 * A container for `ncs-package` components including Callbacks, Applications, Upgrades, and NEDs.
 */
abstract class NcsComponentContainer @Inject constructor(objects: ObjectFactory)
    : ExtensiblePolymorphicDomainObjectContainer<NcsPackageComponent> by objects.polymorphicDomainObjectContainer(NcsPackageComponent::class.java) {

    /**
     * Registers a simple object factory whereby each object of the class [T] is created with [ObjectFactory.newInstance]
     * @param T The type of the component to be generated.
     * @param objects The [ObjectFactory] service.
     */
    private inline fun <reified T: NcsPackageComponent> registerSimpleFactory(objects: ObjectFactory) =
        registerFactory(T::class.java) { name -> objects.newInstance(T::class.java, name) }

    init {
        registerSimpleFactory<JavaCallbackComponent>(objects)

        registerSimpleFactory<JavaApplicationComponent>(objects)
        registerSimpleFactory<PythonApplicationComponent>(objects)

        registerSimpleFactory<JavaUpgradeComponent>(objects)
        registerSimpleFactory<PythonUpgradeComponent>(objects)

        registerSimpleFactory<GenericNedComponent>(objects)
        registerSimpleFactory<CliNedComponent>(objects)
        registerSimpleFactory<NetconfNedComponent>(objects)
        registerSimpleFactory<SnmpNedComponent>(objects)
    }

    /**
     * Registers and configures a Callback component, written in Java.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun callback(name: String, action: Action<JavaCallbackComponent>) =
        register(name, JavaCallbackComponent::class.java, action)

    /**
     * Registers and configures an Application component, written in Java.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun javaApplication(name: String, action: Action<JavaApplicationComponent>) =
        register(name, JavaApplicationComponent::class.java, action)

    /**
     * Registers and configures an Application component, written in Python.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun pythonApplication(name: String, action: Action<PythonApplicationComponent>) =
        register(name, PythonApplicationComponent::class.java, action)

    /**
     * Registers and configures an Upgrade component, written in Java.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun javaUpgrade(name: String, action: Action<JavaUpgradeComponent>) =
        register(name, JavaUpgradeComponent::class.java, action)

    /**
     * Registers and configures an Upgrade component, written in Python.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun pythonUpgrade(name: String, action: Action<PythonUpgradeComponent>) =
        register(name, PythonUpgradeComponent::class.java, action)

    /**
     * Registers and configures a Generic NED component, written in Java.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun genericNed(name: String, action: Action<GenericNedComponent>) =
        register(name, GenericNedComponent::class.java, action)

    /**
     * Registers and configures a CLI NED component, written in Java.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun cliNed(name: String, action: Action<CliNedComponent>) =
        register(name, CliNedComponent::class.java, action)

    /**
     * Registers and configures a NETCONF NED component, written in Java.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun netconfNed(name: String, action: Action<NetconfNedComponent>) =
        register(name, NetconfNedComponent::class.java, action)

    /**
     * Registers and configures an SNMP NED component, written in Java.
     * @param name The component's name.
     * @param action The configuration [action][Action]
     */
    fun snmpNed(name: String, action: Action<SnmpNedComponent>) =
        register(name, SnmpNedComponent::class.java, action)
}
