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
            generate("${model["Type"]}Collection", model, "PrimitiveCollection")
            generate("${model["Type"]}CollectionView", model, "PrimitiveCollectionView")
            generate("${model["Type"]}CollectionLike", model, "PrimitiveCollectionLike")
            generate("Abstract${model["Type"]}Collection", model, "AbstractPrimitiveCollection")

            generate("${model["Type"]}Seq", model, "PrimitiveSeq")
            generate("${model["Type"]}SeqView", model, "PrimitiveSeqView")
            generate("${model["Type"]}SeqLike", model, "PrimitiveSeqLike")
            generate("Indexed${model["Type"]}SeqLike", model, "IndexedPrimitiveSeqLike")
            generate("Abstract${model["Type"]}Seq", model, "AbstractPrimitiveSeq")
        }
    }

    withPackage("kala.collection.immutable.primitive") {
        for (model in Primitives.all) {
            generate("Immutable${model["Type"]}Collection", model, "ImmutablePrimitiveCollection")
            generate("AbstractImmutable${model["Type"]}Collection", model, "AbstractImmutablePrimitiveCollection")

            generate("Immutable${model["Type"]}Seq", model, "ImmutablePrimitiveSeq")
            generate("AbstractImmutable${model["Type"]}Seq", model, "AbstractImmutablePrimitiveSeq")
        }
    }

    withPackage("kala.collection.mutable.primitive") {
        for (model in Primitives.all) {
            generate("Mutable${model["Type"]}Collection", model, "MutablePrimitiveCollection")
            generate("Mutable${model["Type"]}Seq", model, "MutablePrimitiveSeq")
            generate("Mutable${model["Type"]}List", model, "MutablePrimitiveList")
            generate("AbstractMutable${model["Type"]}Collection", model, "AbstractMutablePrimitiveCollection")
            generate("AbstractMutable${model["Type"]}Seq", model, "AbstractMutablePrimitiveSeq")
            generate("AbstractMutable${model["Type"]}List", model, "AbstractMutablePrimitiveList")
        }
    }
}
