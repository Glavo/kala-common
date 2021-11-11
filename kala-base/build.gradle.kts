import kotlin.random.Random

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.freemarker:freemarker:2.3.31")
    }
}

val srcGen = File(buildDir, "src-gen")
val primitives = listOf("Boolean", "Byte", "Short", "Int", "Long", "Float", "Double", "Char")
    .map { type ->
        val res: MutableMap<String, Any?> = mutableMapOf("Type" to type)

        res["PrimitiveType"] = type.toLowerCase()
        res["WrapperType"] = when (type) {
            "Int" -> "Integer"
            "Char" -> "Character"
            else -> type
        }
        res["Var"] = type.first().toLowerCase()
        res["IsSpecialized"] = type == "Int" || type == "Long" || type == "Double"

        if (type != "Boolean") {
            res["Values"] = mapOf(
                "Zero" to when (type) {
                    "Int" -> "0"
                    "Long" -> "0L"
                    "Char" -> "'\\0'"
                    else -> "(${res["PrimitiveType"]}) 0"
                }
            )
        }

        res["LiftToInt"] = type == "Byte" || type == "Short" || type == "Char"

        res.toMap()
    }


sourceSets {
    main {
        java {
            srcDir(srcGen)
        }
    }
}

val generateSources = tasks.create("generateSources") {
    doLast {
        srcGen.mkdirs()
        val conf = freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31)
        conf.setDirectoryForTemplateLoading(file("src/main/template"))
        conf.defaultEncoding = "UTF-8"

        conf.withGenerate("kala.comparator.primitive") {
            for (model in primitives) {
                generate("${model["Type"]}Comparator", model, "PrimitiveComparator")
                generate("${model["Type"]}Comparators", model, "PrimitiveComparators")
            }
        }

        conf.withGenerate("kala.collection.base.primitive") {
            for (model in primitives) {
                generate("${model["Type"]}Arrays", model, "PrimitiveArrays")
                if (model["Type"] != "Boolean") {
                    generate("${model["Type"]}Traversable", model, "PrimitiveTraversable")
                    generate("${model["Type"]}Iterator", model, "PrimitiveIterator")
                    generate("${model["Type"]}Iterators", model, "PrimitiveIterators")
                    generate("Abstract${model["Type"]}Iterator", model, "AbstractPrimitiveIterator")
                }
            }
        }

        conf.withGenerate("kala.control.primitive") {
            for (model in primitives) {
                if (model["Type"] == "Boolean") {
                    continue
                }

                generate("${model["Type"]}Option", model, "PrimitiveOption")
            }
        }

        conf.withGenerate("kala.internal") {
            for (model in primitives) {
                generate("Internal${model["Type"]}ArrayBuilder", model, "InternalPrimitiveArrayBuilder")
            }
        }

        conf.withGenerate("kala.range.primitive") {
            val random = Random(-977415259)
            for (model in primitives) {
                val type = model["Type"] as String
                if (type.let { it == "Boolean" || it == "Float" || it == "Double" }) {
                    continue
                }

                val newModel = model.toMutableMap()
                newModel["SerialVersionUID"] = "${random.nextLong()}L"
                newModel["HashMagic"] = random.nextInt().toString()
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

                generate("${type}Range", newModel, "PrimitiveRange")
            }
        }
    }
}

class GenerateContext(
    private val conf: freemarker.template.Configuration,
    private val srcGen: File,
    private val packageName: String
) {
    private val outputDir = File(srcGen, packageName.replace('.', '/'))

    init {
        outputDir.mkdirs()
    }

    fun generate(fileName: String, model: Any, templateName: String) {
        File(outputDir, "$fileName.java").bufferedWriter().use {
            conf.getTemplate(packageName.replace('.', '/') + "/" + templateName + ".java.ftl")
                .process(model, it)
        }
    }
}

inline fun freemarker.template.Configuration.withGenerate(
    packageName: String,
    action: GenerateContext.() -> Unit
) {
    GenerateContext(this, srcGen, packageName).action()
}

tasks.compileJava {
    dependsOn(generateSources)
}

tasks.sourcesJar {
    dependsOn(generateSources)
}