plugins {
    java
}

val jmhVersion = "1.28"

tasks.compileJava {
    options.release.set(11)
}

tasks.create<JavaExec>("runBenchmark") {
    classpath = sourceSets.main.get().runtimeClasspath

    main = "org.glavo.kala.benchmark.${project.properties["benchmark"]}"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    implementation("org.openjdk.jmh:jmh-core:$jmhVersion")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:$jmhVersion")
}
