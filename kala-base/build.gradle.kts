import kotlin.random.Random

apply {
    plugin(GeneratePlugin::class)
}

tasks.getByName<GenerateTask>("generateSources") {
    withPackage("kala.comparator.primitive") {
        for (model in Primitives.values()) {
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
        }
    }

    withPackage("kala.control.primitive") {
        for (model in Primitives.allWithoutBoolean) {
            generate("${model["Type"]}Option", model, "PrimitiveOption")
            generate("${model["Type"]}Try", model, "PrimitiveTry")
        }
    }

    withPackage("kala.value.primitive") {
        val refHashMagicRandom = Random(-1044014905)
        val lazyValueSerialVersionRandom = Random(-1948426996)
        for (model in Primitives.all) {
            val type = model.type

            generate("${type}Value", model, "PrimitiveValue")
            generate("Mutable${type}Value", model, "MutablePrimitiveValue")

            generate("${type}Ref", model + ("HashMagic" to refHashMagicRandom.nextInt().toString()), "PrimitiveRef")
            generate(
                "Lazy${type}Value",
                model + ("SerialVersionUID" to "${lazyValueSerialVersionRandom.nextLong()}L"),
                "LazyPrimitiveValue"
            )
            generate("LateInit${type}Value", model, "LateInitPrimitiveValue")
            generate("MutableLateInit${type}Value", model, "MutableLateInitPrimitiveValue")
        }
    }

    withPackage("kala.function") {
        for (model in Primitives.all) {
            val type = model.type

            generate("Checked${type}Consumer", model, "CheckedPrimitiveConsumer")
            generate("Checked${type}Supplier", model, "CheckedPrimitiveSupplier")
            generate("${type}Hasher", model, "PrimitiveHasher")

            if (!model.isSpecialized) {
                if (model != Primitives.Boolean)
                    generate("${type}Supplier", model, "PrimitiveSupplier")

                generate("${type}Consumer", model, "PrimitiveConsumer")
                generate("${type}Predicate", model, "PrimitivePredicate")
                generate("${type}Function", model, "PrimitiveFunction")
                generate("${type}UnaryOperator", model, "PrimitiveUnaryOperator")
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
