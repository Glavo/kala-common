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

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public final class ProjectMetadataPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(project.getRootProject().file("gradle/project.properties").toPath())) {
            properties.load(reader);
        } catch (IOException e) {
            throw new GradleException("Failed to load properties", e);
        }

        if (!properties.containsKey("kala.version")) {
            String base = properties.getProperty("kala.version.base");
            String suffix = properties.getProperty("kala.version.suffix");

            if (base == null) {
                throw new GradleException("Missing kala.version.base");
            }

            project.getExtensions().getExtraProperties().set("kala.version",
                    suffix == null ? base : base + suffix);
        }


        properties.forEach((key, value) ->
                project.getExtensions().getExtraProperties().set((String) key, value));

    }
}
