apply {
    plugin(GeneratePlugin::class)
}

dependencies {
    api(project(":kala-base"))
    api(project(":kala-collection"))
}

tasks.getByName<GenerateTask>("generateSources") {
    withPackage("kala.collection.primitive") {
        for (model in Primitives.all) {
            generate("${model["Type"]}CollectionLike", model, "PrimitiveCollectionLike")
            generate("${model["Type"]}Collection", model, "PrimitiveCollection")
            generate("${model["Type"]}SeqLike", model, "PrimitiveSeqLike")
        }
    }
}
