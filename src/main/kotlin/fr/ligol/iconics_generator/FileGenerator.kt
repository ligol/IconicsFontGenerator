package fr.ligol.iconics_generator

import java.io.File

class FileGenerator(private val configuration: IconicGeneratorPluginExtension) {
    fun generateFile(inPath: String, outPath: String) {
        val ressource = javaClass.classLoader.getResource(inPath).readText()
        val outFile = File(outPath)

        outFile.writeText(ressource
                .replace("{fontNameLower}", configuration.name.toLowerCase())
                .replace("{fontName}", configuration.name)
                .replace("{versionCode}", configuration.versionCode)
                .replace("{versionName}", configuration.versionName)
        )
    }
}