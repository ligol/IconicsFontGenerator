package fr.ligol.iconics_generator

object HelloWorld {
    @JvmStatic
    fun main(args: Array<String>) {
        val value = "\"\\e018\"".replace("\\e", "\\ue")
        println(value)
        println(Integer.parseInt(value.substring(1, value.length - 1).substring(2), 16).toChar())
    }
}
