package com.uselessmnemonic.nso.gradle.tasks

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileType
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges

/**
 * A task which compiles Yang modules into a form loadable by the NSO.
 */
abstract class YangCompile : Exec() {

    /**
     * The set of Yang files to be compiled.
     */
    @get:Incremental
    @get:InputFiles
    abstract val inputYangFiles: ConfigurableFileCollection

    /**
     * The location of the NSO installation that willl compile the Yang sources.
     */
    @get:Input
    abstract val ncsDir: Property<String>

    /**
     * The location of the compiled .fxs modules.
     */
    @get:OutputDirectory
    abstract val outputFxsDir: DirectoryProperty

    @TaskAction
    fun execute(inputChanges: InputChanges) {
        val changes = inputChanges.getFileChanges(inputYangFiles)
            .filter { it.fileType !== FileType.DIRECTORY }

        changes.forEach { change ->
            val yangFile = change.file
            val yangModule = yangFile.nameWithoutExtension
            val fxsFile = outputFxsDir.file("$yangModule.fxs").get().asFile

            if (change.changeType === ChangeType.REMOVED) {
                fxsFile.delete()
                return@forEach
            }

            val ncsc = "${ncsDir.get()}/bin/ncsc"
            commandLine(ncsc, "-c", "-o", fxsFile.absolutePath, yangFile.absolutePath)
        }
    }
}
