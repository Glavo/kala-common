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

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReleaseNoteTask extends DefaultTask {

    private String tag;

    public ReleaseNoteTask() {
        this.getOutputs().upToDateWhen(task -> false);
    }

    @Option(option = "tag", description = "git tag")
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Input
    @Optional
    public String getTag() {
        return tag;
    }

    @TaskAction
    public void run() {
        File rootDir = getProject().getRootProject().getRootDir();

        String git;
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
            git = "git.exe";
        } else {
            git = "git";
        }

        List<Version> versions = getProject().getProviders()
                .exec(execSpec -> {
                    execSpec.workingDir(rootDir);
                    execSpec.commandLine(git, "tag");
                }).getStandardOutput().getAsText().get()
                .lines()
                .filter(it -> !it.isBlank())
                .map(Version::parse)
                .filter(Objects::nonNull)
                .sorted()
                .toList();

        if (versions.isEmpty()) {
            throw new GradleException("No versions found");
        }

        Version currentTag;
        Version prevTag;

        if (this.tag == null) {
            if (versions.size() < 2) {
                throw new GradleException("Previous tag not found");
            }

            currentTag = versions.getLast();
            prevTag = versions.get(versions.size() - 2);
        } else {
            currentTag = Version.parse(tag);
            if (currentTag == null) {
                throw new GradleException("Invalid tag: " + tag);
            }

            int currentIdx = versions.lastIndexOf(currentTag);
            if (currentIdx < 0) {
                throw new GradleException("Tag " + tag + " not found");
            }
            if (currentIdx == 0) {
                throw new GradleException("Previous tag of" + tag + " not found");
            }
            prevTag = versions.get(currentIdx - 1);
        }

        System.out.printf("""
                        Changelog for %s
                        ========================
                        
                        [Changelog](https://github.com/Glavo/kala-common/blob/main/CHANGELOG.md#%3$s)
                        
                        ## Add to your project
                        
                        ### Maven
                        
                        ```xml
                        <dependency>
                            <groupId>org.glavo.kala</groupId>
                            <artifactId>kala-common</artifactId>
                            <version>%1$s</version>
                        </dependency>
                        ```
                        
                        ### Gradle
                        
                        ```kotlin
                        implementation("org.glavo.kala:kala-common:%1$s")
                        ```

                        ========================
                        """,
                currentTag, prevTag,
                currentTag.toAnchor()
        );
    }

    private record Version(int[] version) implements Comparable<Version> {

        static @Nullable Version parse(String version) {
            String[] list = version.split("\\.");
            if (list.length < 1) {
                return null;
            }
            int[] result = new int[list.length];
            try {
                for (int i = 0; i < list.length; i++) {
                    result[i] = Integer.parseInt(list[i]);
                }
            } catch (NumberFormatException ignored) {
                return null;
            }

            return new Version(result);
        }

        public String toAnchor() {
            return Arrays.stream(version).mapToObj(Integer::toString).collect(Collectors.joining(""));
        }

        @Override
        public int compareTo(@NotNull ReleaseNoteTask.Version other) {
            for (int i = 0; i < Math.min(this.version.length, other.version.length); i++) {
                int c = Integer.compare(this.version[i], other.version[i]);
                if (c != 0) {
                    return c;
                }
            }
            return Integer.compare(this.version.length, other.version.length);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(version);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Version other && Arrays.equals(this.version, other.version);
        }

        @Override
        public String toString() {
            return Arrays.stream(version).mapToObj(Integer::toString).collect(Collectors.joining("."));
        }
    }
}
