import java.util.Properties

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("org.glavo.compile-module-info-plugin") version "2.0"
}

loadMavenPublishProperties()

allprojects {
    group = "org.glavo.kala"
    version = "0.40.0"// + "-SNAPSHOT"

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
        "org.jetbrains:annotations:23.0.0".also {
            compileOnly(it)
            testImplementation(it)
        }

        testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    }

    tasks.compileJava {
        options.release.set(8)
        options.isWarnings = false
    }

    java {
        withSourcesJar()
        // withJavadocJar()
    }

    val javadocJar = tasks.create<Jar>("javadocJar") {
        group = "build"
        archiveClassifier.set("javadoc")
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
                artifact(javadocJar)

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

fun loadMavenPublishProperties() {
    var secretPropsFile = project.rootProject.file("gradle/maven-central-publish.properties")
    if (!secretPropsFile.exists()) {
        secretPropsFile =
            file(System.getProperty("user.home")).resolve(".gradle").resolve("maven-central-publish.properties")
    }

    if (secretPropsFile.exists()) {
        // Read local.properties file first if it exists
        val p = Properties()
        secretPropsFile.reader().use {
            p.load(it)
        }

        p.forEach { (name, value) ->
            rootProject.ext[name.toString()] = value
        }
    }

    listOf(
        "sonatypeUsername" to "SONATYPE_USERNAME",
        "sonatypePassword" to "SONATYPE_PASSWORD",
        "sonatypeStagingProfileId" to "SONATYPE_STAGING_PROFILE_ID",
        "signing.keyId" to "SIGNING_KEY_ID",
        "signing.password" to "SIGNING_PASSWORD",
        "signing.key" to "SIGNING_KEY"
    ).forEach { (p, e) ->
        if (!rootProject.ext.has(p)) {
            rootProject.ext[p] = System.getenv(e)
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}