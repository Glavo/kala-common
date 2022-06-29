enum class Primitives(private val properties: MutableMap<String, Any?> = mutableMapOf()) :
    Map<String, Any?> by properties {
    Boolean, Byte, Short, Int, Long, Float, Double, Char;

    init {
        val type = this.name

        properties["Type"] = type
        properties["PrimitiveType"] = type.toLowerCase()
        properties["WrapperType"] = when (type) {
            "Int" -> "Integer"
            "Char" -> "Character"
            else -> type
        }
        properties["Var"] = type.first().toLowerCase()
        properties["IsSpecialized"] = type == "Int" || type == "Long" || type == "Double"
        properties["IsIntegral"] = type == "Byte" || type == "Short" || type == "Int" || type == "Long"

        properties["Values"] = mapOf(
            "Zero" to when (type) {
                "Int" -> "0"
                "Long" -> "0L"
                "Char" -> "'\\0'"
                "Boolean" -> null
                else -> "(${properties["PrimitiveType"]}) 0"
            },
            "Default" to when (type) {
                "Int" -> "0"
                "Long" -> "0L"
                "Char" -> "'\\0'"
                "Boolean" -> "false"
                else -> "(${properties["PrimitiveType"]}) 0"
            }
        )

        properties["LiftToInt"] = type == "Byte" || type == "Short" || type == "Char"
    }

    val type: String
        get() = this.name

    val isSpecialized: kotlin.Boolean
        get() = properties["IsSpecialized"] as kotlin.Boolean

    companion object {
        val all = values()
        val allWithoutBoolean = all.filterNot { it == Boolean }
    }
}