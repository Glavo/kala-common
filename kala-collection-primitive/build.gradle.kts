/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    withPackage("kala.collection.primitive.internal.tree") {
        for (model in Primitives.all) {
            if (model != Primitives.Boolean && model != Primitives.Byte)
                generate("${model["Type"]}RedBlackTree", model, "PrimitiveRedBlackTree")
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
            if (model != Primitives.Boolean && model != Primitives.Byte)
                generate("ImmutableSorted${model["Type"]}ArraySet", model, "ImmutableSortedPrimitiveArraySet")
        }
    }

    withPackage("kala.collection.mutable.primitive") {
        for (model in Primitives.all) {
            generate("Mutable${model["Type"]}Collection", model, "MutablePrimitiveCollection")
            generate("AbstractMutable${model["Type"]}Collection", model, "AbstractMutablePrimitiveCollection")

            generate("Mutable${model["Type"]}Seq", model, "MutablePrimitiveSeq")
            generate("Mutable${model["Type"]}List", model, "MutablePrimitiveList")
            generate("AbstractMutable${model["Type"]}Seq", model, "AbstractMutablePrimitiveSeq")
            generate("AbstractMutable${model["Type"]}List", model, "AbstractMutablePrimitiveList")
            generate("AbstractMutable${model["Type"]}ListFactory", model, "AbstractMutablePrimitiveListFactory")

            generate("Mutable${model["Type"]}SeqIterator", model, "MutablePrimitiveSeqIterator")
            generate("Mutable${model["Type"]}ListIterator", model, "MutablePrimitiveListIterator")

            generate("Mutable${model["Type"]}Array", model, "MutablePrimitiveArray")

            if (model != Primitives.Boolean)
                generate("Mutable${model["Type"]}ArrayList", model, "MutablePrimitiveArrayList")

            generate("Mutable${model["Type"]}Set", model, "MutablePrimitiveSet")
            generate("AbstractMutable${model["Type"]}Set", model, "AbstractMutablePrimitiveSet")
            generate("AbstractMutable${model["Type"]}SetFactory", model, "AbstractMutablePrimitiveSetFactory")
            if (model != Primitives.Boolean && model != Primitives.Byte)
                generate("Mutable${model["Type"]}TreeSet", model, "MutablePrimitiveTreeSet")
        }
    }
}
