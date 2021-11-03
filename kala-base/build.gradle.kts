buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.freemarker:freemarker:2.3.31")
    }
}

val srcGen = File(buildDir, "src-gen")
val primitives = listOf("Boolean", "Byte", "Short", "Int", "Long", "Float", "Double")
    .map { type ->
        mapOf(
            "Type" to type,
            "PrimitiveType" to type.toLowerCase(),
            "WrapperType" to if (type == "Int") "Integer" else type,
            "var" to type.first().toLowerCase(),
            "IsSpecialized" to (type == "Int" || type == "Long" || type == "Double")
        )
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

    fun generate(fileName: String, model: Map<*, *>, templateName: String) {
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