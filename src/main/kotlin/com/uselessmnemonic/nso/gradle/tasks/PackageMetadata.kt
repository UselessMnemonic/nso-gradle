package com.uselessmnemonic.nso.gradle.tasks

import com.uselessmnemonic.nso.gradle.dsl.maybeTextNode
import com.uselessmnemonic.nso.gradle.dsl.node
import com.uselessmnemonic.nso.gradle.dsl.nodeNotEmpty
import com.uselessmnemonic.nso.gradle.dsl.textNode
import com.uselessmnemonic.nso.gradle.models.components.UsesJavaClass
import com.uselessmnemonic.nso.gradle.models.components.UsesPythonClass
import com.uselessmnemonic.nso.gradle.models.components.application.NcsApplicationComponent
import com.uselessmnemonic.nso.gradle.models.components.callback.JavaCallbackComponent
import com.uselessmnemonic.nso.gradle.models.components.ned.*
import com.uselessmnemonic.nso.gradle.models.components.upgrade.NcsUpgradeComponent
import com.uselessmnemonic.nso.gradle.plugin.NsoProjectExtension
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * A task that generates package-meta-data.xml
 */
abstract class PackageMetadata : DefaultTask() {

    /**
     * Describes the final XML DOM to write. You should not modify it.
     */
    @get:Input
    val dom: Provider<Element> = project.providers.provider {
        val nso = project.extensions.getByName("nso") as NsoProjectExtension
        val ncsPackage = nso.ncsPackage
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val dom: Document = builder.newDocument()

        dom.node("ncs-package", "http://tail-f.com/ns/ncs-packages") { root ->

            root.textNode("name", ncsPackage.name.get())
            root.textNode("package-version", ncsPackage.packageVersion.get())
            root.maybeTextNode("description", ncsPackage.description.orNull)

            ncsPackage.ncsMinVersions.get().forEach { version ->
                root.textNode("ncs-min-version", version)
            }

            ncsPackage.requiredPackages.get().forEach { name, version ->
                root.node("required-package") { required ->
                    required.textNode("name", name)
                    required.textNode("version", version)
                }
            }

            root.nodeNotEmpty("python-package") { pyOptions ->
                pyOptions.maybeTextNode("vm-name", ncsPackage.pythonOptions.vmName.orNull)
            }

            ncsPackage.supportedNedIds.get().forEach { id ->
                root.textNode("supported-ned-id", id)
            }

            ncsPackage.components.forEach { compImpl ->
                root.node("component") { compNode ->
                    compNode.textNode("name", compImpl.name)
                    compNode.maybeTextNode("description", compImpl.description.orNull)
                    compNode.maybeTextNode("entitlement-tag", compImpl.entitlementTag.orNull)

                    when (compImpl) {
                        is JavaCallbackComponent -> compNode.node("callback") { callNode ->
                            compImpl.javaClassNames.get().forEach { className ->
                                callNode.textNode("java-class-name", className)
                            }
                        }

                        is NcsApplicationComponent -> compNode.node("application") { appNode ->
                            appNode.maybeTextNode("start-phase", compImpl.startPhase.orNull)
                            appNode.maybeTextNode("java-class-name", (compImpl as? UsesJavaClass)?.javaClassName?.orNull)
                            appNode.maybeTextNode("python-class-name", (compImpl as? UsesPythonClass)?.pythonClassName?.orNull)
                        }

                        is NcsUpgradeComponent -> compNode.node("upgrade") { upNode ->
                            upNode.maybeTextNode("java-class-name", (compImpl as? UsesJavaClass)?.javaClassName?.orNull)
                            upNode.maybeTextNode("python-class-name", (compImpl as? UsesPythonClass)?.pythonClassName?.orNull)
                        }

                        is NcsNedComponent -> compNode.node("ned") {
                                nedNode -> when (compImpl) {
                            is GenericNedComponent -> nedNode.node("generic") { generic ->
                                generic.textNode("ned-id", compImpl.nedId.get())
                                generic.textNode("java-class-name", compImpl.javaClassName.get())

                                nedNode.nodeNotEmpty("device") { device ->
                                    device.maybeTextNode("vendor", compImpl.device.vendor.orNull)
                                    device.maybeTextNode("product-family", compImpl.device.productFamily.orNull)
                                }
                            }

                            is CliNedComponent -> nedNode.node("cli") { cli ->
                                cli.textNode("ned-id", compImpl.nedId.get())
                                cli.textNode("java-class-name", compImpl.javaClassName.get())

                                nedNode.nodeNotEmpty("device") { device ->
                                    device.maybeTextNode("vendor", compImpl.device.vendor.orNull)
                                    device.maybeTextNode("product-family", compImpl.device.productFamily.orNull)
                                }
                            }

                            is SnmpNedComponent -> nedNode.node("snmp") { snmp ->
                                snmp.textNode("ned-id", compImpl.nedId.get())
                            }

                            is NetconfNedComponent -> nedNode.node("netconf") { netconf ->
                                netconf.textNode("ned-id", compImpl.nedId.get())
                            }
                        }
                        }
                    }
                }
            }
        }
    }

    /**
     * The final location of project-meta-data.xml
     */
    @get:OutputFile
    abstract val outputXmlFile: RegularFileProperty

    @TaskAction
    fun execute() {

        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount}", "2")
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        transformer.transform(DOMSource(dom.get()), StreamResult(outputXmlFile.get().asFile))
    }
}
