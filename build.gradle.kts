import java.io.RandomAccessFile

buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    }
}

plugins {
    `java-library`
}

allprojects {
    group = "asia.kala"
    version = kalaVersion("0.10.0", release = false)

    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin("com.jfrog.bintray")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        "org.jetbrains:annotations:20.1.0".also {
            compileOnly(it)
            testImplementation(it)
        }

        testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    }

    tasks.compileJava {
        modularity.inferModulePath.set(true)
        options.release.set(9)

        doLast {
            val tree = fileTree(destinationDir)
            tree.include("**/*.class")
            tree.exclude("module-info.class")
            tree.forEach {
                RandomAccessFile(it, "rw").use { rf ->
                    rf.seek(7)   // major version
                    rf.write(52) // java 8
                    rf.close()
                }
            }
        }
    }

    if (this != rootProject) {
        java {
            withSourcesJar()
            withJavadocJar()
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

    tasks.test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }

    configure<com.jfrog.bintray.gradle.BintrayExtension> {
        user = if (project.hasProperty("bintrayUser"))
            project.property("bintrayUser").toString() else System.getenv("BINTRAY_USER")

        key = if (project.hasProperty("bintrayApiKey"))
            project.property("bintrayApiKey").toString() else System.getenv("BINTRAY_API_KEY")

        setPublications("maven")

        pkg.apply {
            repo = "maven"
            name = project.name
            publicDownloadNumbers = true
            vcsUrl = "https://github.com/Glavo/kala-common.git"

            version.apply {
                name = this@allprojects.version.toString()
                desc = "${this@allprojects.name} ${this@allprojects.version}"
                vcsTag = this@allprojects.version.toString()
            }
        }
    }

}

dependencies {
    api(project(":kala-base"))
    api(project(":kala-collection"))
}

fun kalaVersion(base: String, release: Boolean) = if (release) base else "$base-SNAPSHOT"
