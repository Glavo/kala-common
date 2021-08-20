import java.io.RandomAccessFile

plugins {
    `java-library`

}

allprojects {
    group = "org.glavo"
    version = kalaVersion("0.25.0")

    if (this == project(":benchmark")) {
        return@allprojects
    }

    apply {
        plugin("java-library")
        plugin("maven-publish")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        "org.jetbrains:annotations:21.0.1".also {
            compileOnly(it)
            testImplementation(it)
        }

        testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    }

    tasks.compileJava {
        modularity.inferModulePath.set(true)
        options.release.set(9)
        options.isWarnings = false
        doLast {
            val tree = fileTree(destinationDir)
            tree.include("**/*.class")
            tree.exclude("module-info.class")
            tree.forEach {
                RandomAccessFile(it, "rw").use { rf ->
                    rf.seek(7)   // major version
                    rf.write(52)   // java 8
                    rf.close()
                }
            }
        }
    }

    if (this != rootProject) {
        java {
            withSourcesJar()
            // withJavadocJar()
        }
    }

    tasks.withType<Javadoc>().configureEach {
        (options as StandardJavadocDocletOptions).also {
            it.encoding("UTF-8")
            it.addStringOption("link", "https://docs.oracle.com/en/java/javase/11/docs/api/")
            it.addBooleanOption("html5", true)
            it.addStringOption("Xdoclint:none", "-quiet")
        }
    }

    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                groupId = this@allprojects.group.toString()
                version = this@allprojects.version.toString()
                artifactId = this@allprojects.name
                from(components["java"])

                pom {
                    licenses {
                        license {
                            name.set("Apache-2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("glavo")
                            name.set("Glavo")
                            email.set("zjx001202@gmail.com")
                        }
                    }
                }
            }
        }
    }
}

dependencies {
    api(project(":kala-base"))
    api(project(":kala-collection"))
}

sourceSets {
    test {
        java {
            setSrcDirs(listOf("src/test/java", "src/test/template"))
        }
    }
}

tasks.compileTestJava {
    options.release.set(11)
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

fun kalaVersion(base: String) =
        if (System.getProperty("kala.release") == "true" || System.getenv("JITPACK") == "true")
            base
        else
            "$base-SNAPSHOT"
