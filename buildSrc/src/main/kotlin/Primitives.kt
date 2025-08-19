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

import freemarker.template.SimpleScalar
import freemarker.template.TemplateMethodModelEx

enum class Primitives(internal val properties: MutableMap<String, Any?> = mutableMapOf()) :
    Map<String, Any?> by properties {
    Boolean, Byte, Short, Int, Long, Float, Double, Char;

    override val entries: Set<Map.Entry<String, Any?>>
        get() = properties.entries

    val type: String
        get() = this.name

    val isSpecialized: kotlin.Boolean
        get() = properties["IsSpecialized"] as kotlin.Boolean

    val isFloating: kotlin.Boolean
        get() = properties["IsFloating"] as kotlin.Boolean

    fun withNumber(number: kotlin.Int): Map<String, Any?> = properties.mapKeys { "${it.key}$number" }

    companion object {
        val all = values()

        init {
            for (primitive in all) {
                primitive.apply {
                    properties["Type"] = type
                    properties["PrimitiveType"] = type.lowercase()
                    properties["WrapperType"] = when (type) {
                        "Int" -> "Integer"
                        "Char" -> "Character"
                        else -> type
                    }
                    properties["Var"] = type.first().lowercase()
                    properties["IsSpecialized"] = type == "Int" || type == "Long" || type == "Double"
                    properties["IsIntegral"] = type == "Byte" || type == "Short" || type == "Int" || type == "Long"
                    properties["IsFloating"] = type == "Double" || type == "Float"

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

                    properties["PrimitiveEquals"] = TemplateMethodModelEx {
                        require(it.size == 2)

                        val var1 = (it[0] as SimpleScalar).asString
                        val var2 = (it[1] as SimpleScalar).asString

                        when (type) {
                            "Float" -> "Float.floatToIntBits($var1) == Float.floatToIntBits($var2)"
                            "Double" -> "Double.doubleToLongBits($var1) == Double.doubleToLongBits($var2)"
                            else -> "$var1 == $var2"
                        }
                    }

                    properties["PrimitiveNotEquals"] = TemplateMethodModelEx {
                        require(it.size == 2)

                        val var1 = (it[0] as SimpleScalar).asString
                        val var2 = (it[1] as SimpleScalar).asString

                        when (type) {
                            "Float" -> "Float.floatToIntBits($var1) != Float.floatToIntBits($var2)"
                            "Double" -> "Double.doubleToLongBits($var1) != Double.doubleToLongBits($var2)"
                            else -> "$var1 != $var2"
                        }
                    }

                    if (primitive.isFloating) {
                        properties["BitsType"] = if (type == "Float") Int else Long
                        properties["ToBits"] = if (type == "Float") "Float.floatToIntBits" else "Double.doubleToLongBits"
                        properties["ToRawBits"] = if (type == "Float") "Float.floatToRawIntBits" else "Double.doubleToRawLongBits"
                    }
                }
            }
        }

        val allWithoutBoolean = all.filterNot { it == Boolean }

        val specializedPairs = all.filter { it.isSpecialized }.let { specialized ->
            specialized.flatMap { it1 ->
                specialized.map { it2 ->
                    it1.withNumber(1) + it2.withNumber(2)
                }
            }
        }
    }
}