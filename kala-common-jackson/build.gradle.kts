apply {
    plugin(GeneratePlugin::class)
}

dependencies {
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.0")
    compileOnly(project(":kala-base"))
    compileOnly(project(":kala-collection"))
}

tasks.jar {
    manifest.attributes(
        "Kala-Version" to project.version
    )
}

tasks.getByName<GenerateTask>("generateSources") {
    withPackage("kala.jackson") {
        generate("Metadata", mapOf("KalaVersion" to project.version), "Metadata")
    }
}