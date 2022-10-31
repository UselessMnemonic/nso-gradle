package com.uselessmnemonic.nso.gradle.tasks

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileType
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges

/**
 * A task that generates Java namespace bindings according to this package's Yang modules.
 */
abstract class GenerateNamespaces : Exec() {

    /**
     * The location of the compiled Yang modules that contain the desired namespaces.
     */
    @get:Incremental
    @get:InputDirectory
    abstract val inputFxsDir: DirectoryProperty

    /**
     * The name of the Java package for the generated namespace classes.
     */
    @get:Input
    abstract val namespacePackage: Property<String>

    /**
     * The location of the NSO installation that will generate the namespaces.
     */
    @get:Input
    abstract val ncsDir: Property<String>

    /**
     * The location of the output Java files.
     */
    @get:OutputDirectory
    abstract val outputJavaDir: DirectoryProperty

    @TaskAction
    fun execute(inputChanges: InputChanges) {
        val changes = inputChanges.getFileChanges(inputFxsDir)
            .filter { it.fileType !== FileType.DIRECTORY }

        changes.forEach { change ->
            val fxsFile = change.file
            val yangModule = fxsFile.nameWithoutExtension
            val javaFile = outputJavaDir.file("$yangModule.java").get().asFile

            if (change.changeType === ChangeType.REMOVED) {
                javaFile.delete()
                return@forEach
            }

            val ncsc = "${ncsDir.get()}/bin/ncsc"
            commandLine(
                ncsc,
                "--java-disable-prefix",
                "--exclude-enums",
                "--fail-on-warnings",
                "--java-package", namespacePackage.get(),
                "--emit-java", javaFile.absolutePath,
                fxsFile.absolutePath
            )
        }
    }
}
