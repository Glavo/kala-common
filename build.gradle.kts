plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.glavo.load-maven-publish-properties") version "0.1.0"
}

allprojects {
    group = "org.glavo.kala"
    version = "0.72.0" + "-SNAPSHOT"

    description = "Basic components of Kala"

    if (this == project(":benchmark")) {
        return@allprojects
    }

    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin("signing")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnlyApi("org.jetbrains:annotations:24.1.0")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    tasks.compileJava {
        options.release.set(21)
        options.javaModuleVersion.set(project.version.toString())
        options.encoding = "UTF-8"
        options.isWarnings = false
    }

    tasks.withType<Javadoc>().configureEach {
        (options as StandardJavadocDocletOptions).also {
            it.encoding("UTF-8")
            it.addStringOption("link", "https://docs.oracle.com/en/java/javase/21/docs/api/")
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
                    name.set(project.name)
                    description.set(project.description)
                    url.set("https://github.com/Glavo/kala-common")

                    licenses {
                        license {
                            name.set("Apache-2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("glavo")
                            name.set("Glavo")
                            email.set("zjx001202@gmail.com")
                        }
                    }

                    scm {
                        url.set("https://github.com/Glavo/kala-common")
                    }
                }
            }
        }
    }

    if (rootProject.ext.has("signing.key")) {
        signing {
            useInMemoryPgpKeys(
                rootProject.ext["signing.keyId"].toString(),
                rootProject.ext["signing.key"].toString(),
                rootProject.ext["signing.password"].toString(),
            )
            sign(publishing.publications["maven"])
        }
    }
}

dependencies {
    api(project(":kala-base"))
    api(project(":kala-collection"))
    api(project(":kala-collection-primitive"))

    // testImplementation(project(":kala-common-jackson"))
    testImplementation(project(":kala-common-gson"))
}

sourceSets {
    test {
        java {
            setSrcDirs(listOf("src/test/java", "src/test/template", "src/test/util"))
        }
    }
}

tasks.compileTestJava {
    options.release.set(21)
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

tasks.shadowJar {
    manifest.attributes(
        "Automatic-Module-Name" to "kala.common"
    )
}

// ./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}