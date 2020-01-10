
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.whileSelect
import java.lang.StringBuilder
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.time.Instant
import java.time.temporal.TemporalAmount
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlin.time.Duration


fun main() {



    val sb = StringBuilder()
    typez(Person::class.java, sb)

    val (s, map) = Oracle.discover(Person::class.java)
    println("CLAZZ_INFO")
    println(s)
    println("REFFED")
    println(map)

//    Topics.sub("ABC")
//    Topics.sub("ZXC")
//
//    while (true) {
//        val take = Topics.topics.take()
//        println(take)
//    }
//
//    runBlocking {
//        for (i in Topics.topicC) {
//            println(i)
//        }
//    }
}

sealed class Types {

    data class UserDefined(val ref: String) : Types()
    data class Map(val from: Types,  val to: Types) : Types()
}


fun typez(clazz: Class<*>, sb: StringBuilder, i: Int = 0) {


    sb.indent(i).append("name: ").append(clazz.name).append("\n")

    sb.indent(i).append("fields: \n")
    val fieldIndent = i + 2
    clazz.declaredFields.forEach {

        val type = it.genericType

        when {
            type is Class<*> -> sb.append("Cl").indent(fieldIndent).append(it.name, " ", type.typeName, "\n")
            type is ParameterizedType -> sb.append("PT").indent(fieldIndent).append(it.name, " ", type.typeName, "\n")
        }
    }
}

private fun StringBuilder.indent(i: Int): StringBuilder {
    this.append(" ".repeat(i))
    return this
}

fun jsoz(clazz: Class<*>): String  {

    val sb = StringBuilder()
    sb.append("{\n  ").append(clazz.name).append("\n")
    clazz.declaredFields.forEach { toJsonSchema(it, sb, 2) }
    return sb.toString()
}

fun toJsonSchema(field: Field, sb: StringBuilder, indent: Int) {

    sb.append(" ".repeat(indent))
    sb.append("name: ").append(field.name).append("\n")
    sb.append(" ".repeat(indent)).append("type: ")
    when {
        field.type.isArray -> sb.append(" ".repeat(indent)).append("[").append(field.type.componentType).append("]")
        field.type.isPrimitive -> sb.append(" ".repeat(indent)).append(field.type)
        else -> field.type.declaredFields?.forEach { toJsonSchema(it, sb, indent + 2) }
    }
    sb.append(" ".repeat(indent)).append("fields").append(" -> ").append("{\n")
    sb.append("\n").append(" ".repeat(indent)).append("}")

}






object Topics {

    val topics = LinkedBlockingQueue<String>()
    val topicC = Channel<String>(5)

    fun sub(name: String) {

        sim {
            val now = Instant.now()
            topics.put("FROM $name $it")
            println("Sent in ${Instant.now().minusNanos((now.nano).toLong()).nano} ns")
        }

//        sim {
//            GlobalScope.launch {
//                val now = Instant.now()
//                topicC.send("FROM $name $it")
//                println("Sent in ${Instant.now().minusNanos((now.nano).toLong()).nano} ns")
//            }
//        }

    }


    // Imagine this is actually a method from a Java lib. Takes callback and does its thing.
    private fun sim(cb: (String) -> Unit): () -> Unit  {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            cb(Random.nextInt().toString())
        }, 0, 500, TimeUnit.MILLISECONDS)
        return { println("Cancel token invoked.")}
    }
}