package fr.ligol.iconics_generator

import org.gradle.api.Plugin
import org.gradle.api.Project


@Suppress("RedundantVisibilityModifier")
public class IconicsGeneratorPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val rootProject = project.rootProject
        if (project != rootProject) {
            throw IllegalStateException(
                    "Register the 'fr.ligol.iconic_generator' plugin only once " +
                            "in the root project build.gradle.")
        }

        rootProject.extensions.create("iconics", IconicGeneratorPluginExtension::class.java)

        with(project) {
            tasks.create("generateIconicsProject", GenerateIconicsProjectTask::class.java)
        }
    }
}