plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("org.glavo.compile-module-info-plugin") version "2.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.glavo.load-maven-publish-properties") version "0.1.0"
}

allprojects {
    group = "org.glavo.kala"
    version = "0.68.0" + "-SNAPSHOT"

    description = "Basic components of Kala"

    if (this == project(":benchmark")) {
        return@allprojects
    }

    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin("signing")
        plugin("org.glavo.compile-module-info-plugin")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnlyApi("org.jetbrains:annotations:24.0.1")
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    }

    var multiRelease = false;

    for (multiVersion in 9..21) {
        if (!project.file("src/main/java$multiVersion").exists()) {
            continue
        }

        multiRelease = true

        val multiSourceSet = sourceSets.create("java$multiVersion") {
            java.srcDir("src/main/java$multiVersion")
        }

        tasks.named<JavaCompile>("compileJava${multiVersion}Java") {
            sourceCompatibility = "$multiVersion"
            targetCompatibility = "$multiVersion"
        }

        dependencies {
            "java${multiVersion}CompileOnly"("org.jetbrains:annotations:23.1.0")
            "java${multiVersion}Implementation"(sourceSets.main.get().output.classesDirs)
        }

        tasks.jar {
            into("META-INF/versions/${multiVersion}") {
                from(multiSourceSet.output)
            }

            manifest.attributes(
                "Multi-Release" to "true"
            )
        }
    }

    if (multiRelease) {
        tasks.jar {
            manifest.attributes(
                "Multi-Release" to "true"
            )
        }
    }

    tasks.compileJava {
        sourceCompatibility = "9"
        options.release.set(8)
        options.isWarnings = false
    }

    val sourcesJar = tasks.create<Jar>("sourcesJar") {
        group = "build"
        archiveClassifier.set("sources")

        from(sourceSets.main.get().allSource)

//        if (hasJava17Sources) {
//            into("META-INF/versions/17") {
//                from(sourceSets["java17"].allSource)
//            }
//        }
    }

    val javadocJar = tasks.create<Jar>("javadocJar") {
        group = "build"
        archiveClassifier.set("javadoc")
    }

    tasks.withType<org.glavo.mic.tasks.CompileModuleInfo> {
        moduleVersion = project.version.toString()
    }

    tasks.withType<Javadoc>().configureEach {
        (options as StandardJavadocDocletOptions).also {
            it.encoding("UTF-8")
            it.addStringOption("link", "https://docs.oracle.com/en/java/javase/17/docs/api/")
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
                artifact(javadocJar)
                artifact(sourcesJar)

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
    options.release.set(17)
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

tasks.shadowJar {
    manifest.attributes(
        "Automatic-Module-Name" to "kala.common",
        "Multi-Release" to "true"
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