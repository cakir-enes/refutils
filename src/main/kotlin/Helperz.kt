import java.lang.StringBuilder
import java.lang.reflect.ParameterizedType


object Oracle {

    val knowledge = mutableMapOf<Class<*>, String>()

    fun discover(clazz: Class<*>): Pair<String, Map<String, String>> {

        val sb = StringBuilder()
        val reffedClasses = discover(clazz, sb)
        val reffedClassInfos = mutableMapOf<String, String>()
        reffedClasses.forEach { reffedClassInfos[it.name] = knowledge.getOrDefault(it, "UNKNOWN") }
        return Pair(sb.toString(), reffedClassInfos)
    }

    private fun discover(clazz: Class<*>, sb: StringBuilder, i: Int = 0): Set<Class<*>> {

        if (knowledge.contains(clazz))
            return emptySet()
        knowledge[clazz] = "DISCOVERING"
        sb.indent(i).append("{name: ", clazz.name, "\n")
        sb.indent(i).append("fields: [\n")

        val reffedClasses = mutableSetOf<Class<*>>()

        val fieldIndent = i + 2


        val classHandler: (Class<*>) -> Unit = {

            if (it.isArray) {

                val compType = it.componentType
                sb.append("[]", compType.properName)

                if (!primitive(compType)) reffedClasses.add(compType)
            } else {

                if (!primitive(it)) reffedClasses.add(it)

                sb.append(it.properName)
            }
        }

        fun genericClassHandler(it: ParameterizedType) {
            val rawType = it.rawType as Class<*>

            when {
                primitive(rawType) -> sb.append(it.typeName)

                Map::class.java.isAssignableFrom(rawType) -> {

                    sb.append("Map<")

                    val keyType = it.actualTypeArguments[0]
                    val valType = it.actualTypeArguments[1]

                    when {
                        keyType is ParameterizedType && valType is ParameterizedType -> {
                            genericClassHandler(keyType)
                            sb.append(",")
                            genericClassHandler(valType)
                        }
                        keyType is ParameterizedType -> {
                            genericClassHandler(keyType)
                            sb.append(",")
                            classHandler(valType as Class<*>)
                        }
                        valType is ParameterizedType -> {
                            classHandler(keyType as Class<*>)
                            sb.append(",")
                            genericClassHandler(valType)
                        }
                        else -> println("WTFDUDE")

                    }

                    sb.append(">")
                }
                Set::class.java.isAssignableFrom(rawType) -> {
                    sb.append("Set", "<")
                    when (val genType = it.actualTypeArguments[0]) {
                        is ParameterizedType -> genericClassHandler(genType)
                        else -> classHandler(genType as Class<*>)
                    }
                    sb.append(">")
                }
                List::class.java.isAssignableFrom(rawType) -> {
                    sb.append(
                        "[]"
                    )
                    when (val genType = it.actualTypeArguments[0]) {
                        is ParameterizedType -> genericClassHandler(genType)
                        else -> classHandler(genType as Class<*>)
                    }
                }
                else -> {
                    println("GENERIC CLASS HANDLER ELSE with $it")

                    sb.append(it.rawType.typeName)
                    sb.append("<")
                    val genericRaw = StringBuilder()
                    val typeArg = it.actualTypeArguments[0]
                    if (typeArg is ParameterizedType) {
                        genericClassHandler(typeArg)
//                        reffedClasses.add(typeArg)
                        println()
                    } else {
                        reffedClasses.addAll(discover(typeArg as Class<*>, genericRaw))
                    }
                    sb.append(">")
                    val genSb = StringBuilder()
                    reffedClasses.addAll(discover(it.rawType as Class<*>, genSb))

                }
            }
        }

        clazz.declaredFields.forEach { field ->

            sb.indent(fieldIndent).append("name: ", field.name, "\n")
            sb.indent(fieldIndent).append("type: ")

            when (val type = field.genericType) {
                is Class<*> -> classHandler(type)
                is ParameterizedType -> genericClassHandler(type)
            }
            sb.append("\n")
        }
        sb.append("]}")
        knowledge[clazz] = sb.toString()
        return reffedClasses
    }

    private val <T> Class<T>.properName: String?
        get() {

            val numbers = setOf(Long::class.java, Integer::class.java, Float::class.java, Double::class.java)

            return when {
                numbers.contains(this) -> "number"
                this == String::class.java -> "string"
                else -> this.name
            }
        }

    private fun primitive(clazz: Class<*>): Boolean {
        val primitives = setOf(Long::class.java, Integer::class.java, String::class.java)
        return (clazz.isPrimitive || primitives.contains(clazz))
    }

    private fun StringBuilder.indent(i: Int): StringBuilder {
        this.append(" ".repeat(i))
        return this
    }
}