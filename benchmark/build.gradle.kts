import java.nio.file.*
import java.util.Date
import java.text.SimpleDateFormat

plugins {
    java
}

val jmhVersion = "1.37"
val jolVersion = "0.17"

tasks.compileJava {
    options.release.set(21)
}

tasks.create<JavaExec>("runBenchmark") {
    classpath = sourceSets.main.get().runtimeClasspath

    mainClass.set("kala.benchmark.Main")

    systemProperties = mapOf(
            "file.encoding" to "UTF-8"
    )

    val logDir = project.layout.buildDirectory.asFile.get().toPath().resolve("logs").also {
        if (Files.notExists(it)) {
            Files.createDirectories(it)
        }
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd_HHmmss")
    val fo = Files.newOutputStream(logDir.resolve("${dateFormat.format(Date())}.log"))
    standardOutput = org.apache.tools.ant.util.TeeOutputStream(System.out, fo)
    errorOutput = org.apache.tools.ant.util.TeeOutputStream(System.err, fo)

    args = (project.properties["benchmark"] as? String)?.split(';') ?: listOf()

    doLast { fo.close() }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    implementation("org.openjdk.jol:jol-core:$jolVersion")
    implementation("org.openjdk.jmh:jmh-core:$jmhVersion")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:$jmhVersion")
}
