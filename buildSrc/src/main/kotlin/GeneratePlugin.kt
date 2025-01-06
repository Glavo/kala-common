/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        val generateSources = project.tasks.register<GenerateTask>("generateSources") {
            templateDirectory = project.file("src/main/template").absolutePath
            generateSourceDirectory = srcGen.absolutePath
        }

        project.tasks["compileJava"].dependsOn(generateSources)
        project.tasks["sourcesJar"].dependsOn(generateSources)
    }
}
