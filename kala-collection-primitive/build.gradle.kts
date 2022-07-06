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
            generate("Abstract${model["Type"]}CollectionView", model, "AbstractPrimitiveCollectionView")

            generate("${model["Type"]}Seq", model, "PrimitiveSeq")
            generate("${model["Type"]}SeqView", model, "PrimitiveSeqView")
            generate("${model["Type"]}SeqLike", model, "PrimitiveSeqLike")
            generate("Indexed${model["Type"]}SeqLike", model, "IndexedPrimitiveSeqLike")
            generate("Indexed${model["Type"]}Seq", model, "IndexedPrimitiveSeq")
            generate("Abstract${model["Type"]}Seq", model, "AbstractPrimitiveSeq")
            generate("Abstract${model["Type"]}SeqView", model, "AbstractPrimitiveSeqView")

            generate("${model["Type"]}SeqIterator", model, "PrimitiveSeqIterator")
            generate("Abstract${model["Type"]}SeqIterator", model, "AbstractPrimitiveSeqIterator")

            generate("${model["Type"]}ArraySeq", model, "PrimitiveArraySeq")
        }
    }

    withPackage("kala.collection.primitive.internal") {
        for (model in Primitives.all) {
            generate("${model["Type"]}SeqIterators", model, "PrimitiveSeqIterators")
        }
    }

    withPackage("kala.collection.primitive.internal.view") {
        for (model in Primitives.all) {
            generate("${model["Type"]}CollectionViews", model, "PrimitiveCollectionViews")
            generate("${model["Type"]}SeqViews", model, "PrimitiveSeqViews")
        }
    }

    withPackage("kala.collection.immutable.primitive") {
        for (model in Primitives.all) {
            generate("Immutable${model["Type"]}Collection", model, "ImmutablePrimitiveCollection")
            generate("AbstractImmutable${model["Type"]}Collection", model, "AbstractImmutablePrimitiveCollection")

            generate("Immutable${model["Type"]}Seq", model, "ImmutablePrimitiveSeq")
            generate("AbstractImmutable${model["Type"]}Seq", model, "AbstractImmutablePrimitiveSeq")

            generate("Immutable${model["Type"]}Array", model, "ImmutablePrimitiveArray")
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

            generate("Mutable${model["Type"]}SeqIterator", model, "MutablePrimitiveSeqIterator")
            generate("Mutable${model["Type"]}ListIterator", model, "MutablePrimitiveListIterator")

            generate("Mutable${model["Type"]}Array", model, "MutablePrimitiveArray")
        }
    }
}
