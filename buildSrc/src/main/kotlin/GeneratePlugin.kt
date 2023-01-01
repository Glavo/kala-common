import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.*

class GeneratePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val srcGen = project.buildDir.resolve("src-gen")

        project.extensions.getByType(JavaPluginExtension::class.java)
            .sourceSets
            .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
            .java {
                srcDir(srcGen)
            }

        val generateSources = project.tasks.create<GenerateTask>("generateSources") {
            templateDirectory = project.file("src/main/template").absolutePath
            generateSourceDirectory = srcGen.absolutePath
        }

        project.tasks["compileJava"].dependsOn(generateSources)
        project.tasks["sourcesJar"].dependsOn(generateSources)

        if (project.file("src/main/template-java17").exists()) {
            val srcGen17 = project.buildDir.resolve("src-gen-java17")
            val generateJava17Sources = project.tasks.create<GenerateTask>("generateJava17Sources") {
                templateDirectory = project.file("src/main/template-java17").absolutePath
                generateSourceDirectory = srcGen17.absolutePath
            }

            project.extensions.getByType(JavaPluginExtension::class.java)
                .sourceSets
                .getByName("java17")
                .java {
                    srcDir(srcGen17)
                }

            project.tasks["compileJava17Java"].dependsOn(generateJava17Sources)
        }
    }
}
