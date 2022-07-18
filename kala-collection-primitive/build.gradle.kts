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

            generate("${model["Type"]}Set", model, "PrimitiveSet")
            generate("${model["Type"]}SetView", model, "PrimitiveSetView")
            generate("${model["Type"]}SetLike", model, "PrimitiveSetLike")
            generate("Abstract${model["Type"]}Set", model, "AbstractPrimitiveSet")
            generate("Abstract${model["Type"]}SetView", model, "AbstractPrimitiveSetView")
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
            generate("${model["Type"]}SetViews", model, "PrimitiveSetViews")
        }
    }

    withPackage("kala.collection.immutable.primitive") {
        for (model in Primitives.all) {
            generate("Immutable${model["Type"]}Collection", model, "ImmutablePrimitiveCollection")
            generate("AbstractImmutable${model["Type"]}Collection", model, "AbstractImmutablePrimitiveCollection")

            generate("Immutable${model["Type"]}Seq", model, "ImmutablePrimitiveSeq")
            generate("AbstractImmutable${model["Type"]}Seq", model, "AbstractImmutablePrimitiveSeq")

            generate("Immutable${model["Type"]}Array", model, "ImmutablePrimitiveArray")

            generate("Immutable${model["Type"]}Set", model, "ImmutablePrimitiveSet")
            generate("AbstractImmutable${model["Type"]}Set", model, "AbstractImmutablePrimitiveSet")
        }
    }

    withPackage("kala.collection.mutable.primitive") {
        for (model in Primitives.all) {
            val m = if (model == Primitives.Boolean) {
                model + ("DefaultMutableSet" to "DefaultMutableBooleanSet")
            } else {
                model
            }

            generate("Mutable${model["Type"]}Collection", m, "MutablePrimitiveCollection")
            generate("AbstractMutable${model["Type"]}Collection", m, "AbstractMutablePrimitiveCollection")

            generate("Mutable${model["Type"]}Seq", m, "MutablePrimitiveSeq")
            generate("Mutable${model["Type"]}List", m, "MutablePrimitiveList")
            generate("AbstractMutable${model["Type"]}Seq", m, "AbstractMutablePrimitiveSeq")
            generate("AbstractMutable${model["Type"]}List", m, "AbstractMutablePrimitiveList")
            generate("AbstractMutable${model["Type"]}ListFactory", m, "AbstractMutablePrimitiveListFactory")

            generate("Mutable${model["Type"]}SeqIterator", m, "MutablePrimitiveSeqIterator")
            generate("Mutable${model["Type"]}ListIterator", m, "MutablePrimitiveListIterator")

            generate("Mutable${model["Type"]}Array", m, "MutablePrimitiveArray")
            generate("Mutable${model["Type"]}ArrayList", m, "MutablePrimitiveArrayList")

            generate("Mutable${model["Type"]}Set", m, "MutablePrimitiveSet")
            generate("AbstractMutable${model["Type"]}Set", m, "AbstractMutablePrimitiveSet")
            generate("AbstractMutable${model["Type"]}SetFactory", m, "AbstractMutablePrimitiveSetFactory")
        }
    }
}
