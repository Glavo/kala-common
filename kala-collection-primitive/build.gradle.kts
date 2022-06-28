apply {
    plugin(GeneratePlugin::class)
}

dependencies {
    api(project(":kala-base"))
    api(project(":kala-collection"))
}