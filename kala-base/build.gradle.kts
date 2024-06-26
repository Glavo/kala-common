/*
 * Copyright 2024 Glavo
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

import kotlin.random.Random

apply {
    plugin(GeneratePlugin::class)
}

tasks.getByName<GenerateTask>("generateSources") {
    withPackage("kala.comparator.primitive") {
        for (model in Primitives.all) {
            generate("${model["Type"]}Comparator", model, "PrimitiveComparator")
            generate("${model["Type"]}Comparators", model, "PrimitiveComparators")
        }
    }

    withPackage("kala.collection.base.primitive") {
        for (model in Primitives.all) {
            val type = model.type

            val newModel = model.toMutableMap()
            newModel["PrimitiveIteratorType"] =
                if (model["IsSpecialized"] as Boolean) "java.util.PrimitiveIterator.Of$type" else "${type}Iterator"

            generate("${type}Arrays", newModel, "PrimitiveArrays")
            generate("${type}Traversable", newModel, "PrimitiveTraversable")
            generate("${type}Iterator", newModel, "PrimitiveIterator")
            generate("${type}Iterators", newModel, "PrimitiveIterators")
            generate("${type}Growable", newModel, "PrimitiveGrowable")
            generate("Abstract${type}Iterator", newModel, "AbstractPrimitiveIterator")
        }
    }

    withPackage("kala.collection.factory.primitive") {
        for (model in Primitives.all) {
            val type = model.type

            generate("${type}CollectionFactory", model, "PrimitiveCollectionFactory")
            generate("${type}CollectionBuilder", model, "PrimitiveCollectionBuilder")
        }
    }

    withPackage("kala.control.primitive") {
        for (model in Primitives.allWithoutBoolean) {
            generate("${model["Type"]}Option", model, "PrimitiveOption")
            generate("${model["Type"]}Try", model, "PrimitiveTry")
        }
    }

    withPackage("kala.value.primitive") {
        for (model in Primitives.all) {
            val type = model.type

            generate("${type}Value", model, "PrimitiveValue")
            generate("Mutable${type}Value", model, "MutablePrimitiveValue")
            generate("Abstract${type}Value", model, "AbstractPrimitiveValue")
            generate("Default${type}Value", model, "DefaultPrimitiveValue")
            generate("Delegate${type}Value", model, "DelegatePrimitiveValue")
            generate("AbstractMutable${type}Value", model, "AbstractMutablePrimitiveValue")
            generate("DelegateMutable${type}Value", model, "DelegateMutablePrimitiveValue")

            generate("${type}Var", model, "PrimitiveVar")
            generate("Volatile${type}Var", model, "VolatilePrimitiveVar")
            generate("Lazy${type}Value", model, "LazyPrimitiveValue")
            generate("LateInit${type}Value", model, "LateInitPrimitiveValue")
            generate("MutableLateInit${type}Value", model, "MutableLateInitPrimitiveValue")

            if (model == Primitives.Int || model == Primitives.Long)
                generate("Atomic${type}Var", model, "AtomicPrimitiveVar")
        }
    }

    withPackage("kala.function") {
        for (model in Primitives.all) {
            val type = model.type

            generate("Checked${type}Consumer", model, "CheckedPrimitiveConsumer")
            generate("Checked${type}Supplier", model, "CheckedPrimitiveSupplier")
            generate("${type}Hasher", model, "PrimitiveHasher")
            generate("Obj${type}BiFunction", model, "ObjPrimitiveBiFunction")
            generate("${type}ObjBiFunction", model, "PrimitiveObjBiFunction")
            generate("Indexed${type}UnaryOperator", model, "IndexedPrimitiveUnaryOperator")

            if (!model.isSpecialized) {
                if (model != Primitives.Boolean)
                    generate("${type}Supplier", model, "PrimitiveSupplier")

                generate("${type}Consumer", model, "PrimitiveConsumer")
                generate("${type}Predicate", model, "PrimitivePredicate")
                generate("${type}Function", model, "PrimitiveFunction")
                generate("${type}UnaryOperator", model, "PrimitiveUnaryOperator")
                generate("${type}BinaryOperator", model, "PrimitiveBinaryOperator")
            }
        }
    }

    withPackage("kala.tuple.primitive") {
        for (model in Primitives.specializedPairs) {
            val className = ((if (model["Type1"] == model["Type2"]) model["Type1"].toString() else "${model["Type1"]}${model["Type2"]}") + "Tuple2")

            generate(className, model + ("ClassName" to className), "PrimitiveTuple2")
        }

        for (m1 in Primitives.all) {
            for (m2 in Primitives.all) {
                if (m1 == m2 && m1 == Primitives.Boolean) continue

                val model = mutableMapOf<String, Any?>()
                model += m1.withNumber(1)
                model += m2.withNumber(2)
            }
        }
    }

    withPackage("kala.internal") {
        for (model in Primitives.all) {
            generate("Internal${model["Type"]}ArrayBuilder", model, "InternalPrimitiveArrayBuilder")
        }
    }

    withPackage("kala.range.primitive") {
        val random = Random(-977415259)
        for (model in Primitives.allWithoutBoolean) {
            val type = model.type

            val newModel = model.toMutableMap()
            newModel["SerialVersionUID"] = "${random.nextLong()}L"
            newModel["HashMagic"] = random.nextInt().toString()

            if (type == "Float" || type == "Double") {
                generate("${type}Range", newModel, "FloatingRange")
            } else {
                newModel["StepType"] = when (type) {
                    "Int", "Long" -> "long"
                    else -> "int"
                }
                newModel["MaxStep"] = when (type) {
                    "Byte" -> "Byte.MAX_VALUE - Byte.MIN_VALUE"
                    "Short" -> "Short.MAX_VALUE - Short.MIN_VALUE"
                    "Char" -> "Character.MAX_VALUE"
                    "Int" -> "(long) Integer.MAX_VALUE - Integer.MIN_VALUE"
                    "Long" -> "Long.MAX_VALUE"
                    else -> throw AssertionError()
                }
                newModel["MaxReverseStep"] = when (type) {
                    "Long" -> "Long.MIN_VALUE"
                    else -> "-MAX_STEP"
                }

                generate("${type}Range", newModel, "IntegralRange")
            }
        }
    }
}
