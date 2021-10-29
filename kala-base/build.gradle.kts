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


        // PrimitiveComparator
        run {
            val outputDir = File(srcGen, "kala/comparator/primitive/")
            outputDir.mkdirs()

            val comparatorTemplate = conf.getTemplate("kala/comparator/primitive/PrimitiveComparator.java.ftl")
            val comparatorsTemplate = conf.getTemplate("kala/comparator/primitive/PrimitiveComparators.java.ftl")

            for (primitive in primitives) {
                val model = mapOf(
                    "Type" to primitive,
                    "PrimitiveType" to primitive.toLowerCase(),
                    "WrapperType" to if (primitive == "Int") "Integer" else primitive,
                    "var" to primitive.first().toLowerCase()
                )

                File(outputDir, primitive + "Comparator.java").bufferedWriter().use {
                    comparatorTemplate.process(model, it)
                }
                File(outputDir, primitive + "Comparators.java").bufferedWriter().use {
                    comparatorsTemplate.process(model, it)
                }
            }
        }
    }
}

tasks.compileJava {
    dependsOn(generateSources)
}