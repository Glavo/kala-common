import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.*

class GeneratePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val buildDir = project.layout.buildDirectory.asFile.get()

        val srcGen = buildDir.resolve("src-gen")

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

        for (multiVersion in 9..21) {
            if (project.file("src/main/template-java$multiVersion").exists()) {
                val srcGenMulti = buildDir.resolve("src-gen-java$multiVersion")
                val generateJavaMultiSources = project.tasks.create<GenerateTask>("generateJava${multiVersion}Sources") {
                    templateDirectory = project.file("src/main/template-java$multiVersion").absolutePath
                    generateSourceDirectory = srcGenMulti.absolutePath
                }

                project.extensions.getByType(JavaPluginExtension::class.java)
                    .sourceSets
                    .getByName("java$multiVersion")
                    .java {
                        srcDir(srcGenMulti)
                    }

                project.tasks["compileJava${multiVersion}Java"].dependsOn(generateJavaMultiSources)
                project.tasks["sourcesJar"].dependsOn(generateJavaMultiSources)
            }
        }
    }
}
