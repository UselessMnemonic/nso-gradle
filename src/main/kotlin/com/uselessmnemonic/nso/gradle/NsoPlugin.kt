package com.uselessmnemonic.nso.gradle

import com.uselessmnemonic.nso.gradle.plugin.NsoProjectExtension
import com.uselessmnemonic.nso.gradle.tasks.GenerateNamespaces
import com.uselessmnemonic.nso.gradle.tasks.PackageMetadata
import com.uselessmnemonic.nso.gradle.tasks.YangCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar

/**
 * The NSO Project plugin.
 */
class NsoPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        // Make some default providers for project configuration
        val projectNameProvider = project.providers.provider(project::getName)
        val projectVersionNameProvider = project.providers.provider(project::getVersion).map(Any::toString)
        val projectDescriptionProvider = project.providers.provider(project::getDescription)
        val projectGroupProvider = project.providers.provider(project::getGroup)
        val fxsOutDirProvider = project.layout.buildDirectory.dir("fxs")
        val generatedNamespacesDir = project.layout.buildDirectory.dir("generated/sources/namespaces")
        val packageMetadataPath = project.layout.buildDirectory.file("artifacts/package-meta-data.xml")

        // Instantiate the default nso configuration
        val nso = project.extensions.create("nso", NsoProjectExtension::class.java)
        nso.ncsDir.convention(project.providers.environmentVariable("NCS_DIR"))

        val ncsPackage = nso.ncsPackage
        ncsPackage.name.convention(projectNameProvider)
        ncsPackage.packageVersion.convention(projectVersionNameProvider)
        ncsPackage.description.convention(projectDescriptionProvider)

        // Project depends on NSO-provided runtime JARs.
        project.repositories.flatDir {
            val ncsJavaJars = nso.ncsDir.map { "$it/java/jar" }
            it.dir(ncsJavaJars)
        }

        // Define the yang source sets (only main is supported)
        val projectSrcSets = project.extensions.getByName("sourceSets") as SourceSetContainer
        projectSrcSets.named("main") { main ->
            val yangSrcSet = nso.sourceSets.create("main")
            val yangSrcDir = project.layout.projectDirectory.dir("src").dir("main").dir("yang")

            yangSrcSet.yang.srcDir(yangSrcDir)
            yangSrcSet.yang.destinationDirectory.set(fxsOutDirProvider)
            main.allSource.source(yangSrcSet.yang)
        }

        // Generate yang compile tasks for each source set (only main is supported)
        project.tasks.register("compileYang", YangCompile::class.java) { task ->
            task.group = "other"
            task.description = "Compiles yang main source."

            task.ncsDir.convention(nso.ncsDir)

            val yangSrcSet = nso.sourceSets.getByName("main")
            task.inputYangFiles.setFrom(yangSrcSet.yang)
            task.inputYangFiles.filter { it.name.endsWith(".yang") }
            task.outputFxsDir.set(yangSrcSet.yang.destinationDirectory)
        }

        // Generate namespace task for each source set (only main is supported)
        project.tasks.register("namespaces", GenerateNamespaces::class.java) { task ->
            task.group = "other"
            task.description = "Generates yang namespaces main source."
            task.dependsOn("compileYang")

            task.ncsDir.convention(nso.ncsDir)

            val yangSrcSet = nso.sourceSets.getByName("main")
            task.inputFxsDir.set(yangSrcSet.yang.destinationDirectory)

            val correctedName = projectNameProvider.map { it.lowercase().replace("-", "") }
            val fullNamespace = project.providers.provider { "${projectGroupProvider.get()}.${correctedName.get()}.namespaces" }
            task.namespacePackage.convention(fullNamespace)

            val namespaceAsDir = task.namespacePackage.map { it.replace(".", "/") }
            val fullOutputDir = generatedNamespacesDir.map { it.dir(namespaceAsDir.get()) }
            task.outputJavaDir.set(fullOutputDir)
        }

        // Java sources also require including generated namespaces
        projectSrcSets.named("main") { main ->
            main.java.srcDir(generatedNamespacesDir)
        }
        project.tasks.findByName("compileJava")?.dependsOn("namespaces")
        project.tasks.findByName("compileKotlin")?.dependsOn("namespaces")

        // A project-meta-data.xml package descriptor should be generated
        project.tasks.register("packageMetadata", PackageMetadata::class.java) { task ->
            task.group = "build"
            task.description = "Generates package-meta-data.xml"

            task.outputXmlFile.convention(packageMetadataPath)
        }

        project.tasks.getByName("build").dependsOn("packageMetadata")

        // Targets to generate jar artifacts for final packaging
        project.tasks.register("privateJar", Jar::class.java) { task ->
            task.group = "build"
            task.description = "Generates the NCS private archive that contains Java application data."
            task.dependsOn("classes", "packageMetadata")

            task.destinationDirectory.set(project.layout.buildDirectory.dir("artifacts/private-jar"))
            task.archiveBaseName.convention(nso.ncsPackage.name)
            task.archiveVersion.convention(nso.ncsPackage.packageVersion)
            task.archiveExtension.convention("jar")

            task.includeEmptyDirs = false
            task.from(project.layout.buildDirectory.dir("classes/java/main")) {
                it.exclude("**/namespaces/*.class")
                it.include("**/*.class")
            }

            task.from(project.layout.buildDirectory.dir("classes/kotlin/main")) {
                it.include("**/*.class")
            }

            task.from(project.layout.buildDirectory.dir("artifacts")) {
                it.include("package-meta-data.xml")
            }
        }

        project.tasks.register("sharedJar", Jar::class.java) { task ->
            task.group = "build"
            task.description = "Generates the NCS shared archive that contains namespace bindings."
            task.dependsOn("classes")

            task.destinationDirectory.set(project.layout.buildDirectory.dir("artifacts/shared-jar"))
            task.archiveBaseName.convention(nso.ncsPackage.name.map { "${it}-ns" })
            task.archiveVersion.convention(nso.ncsPackage.packageVersion)
            task.archiveExtension.convention("jar")

            task.includeEmptyDirs = false
            task.from(project.layout.buildDirectory.dir("classes/java/main")) {
                it.include("**/namespaces/*.class")
            }
        }
    }
}
