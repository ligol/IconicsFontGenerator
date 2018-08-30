package fr.ligol.iconics_generator

import com.squareup.kotlinpoet.FileSpec
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File


open class GenerateIconicsProjectTask : org.gradle.api.DefaultTask() {

    init {
        group = "help"
    }

    val extension: IconicGeneratorPluginExtension
        @Input get() {
            return project.extensions.findByType(IconicGeneratorPluginExtension::class.java)?: IconicGeneratorPluginExtension()
        }

    @TaskAction
    fun generateIconics() {
        val fontasticApiKey = extension.fontasticApiKey
        val parser = CssParser(downloadFile("https://file.myfontastic.com/$fontasticApiKey/icons.css")!!.string())
        val itemMap = parser.getFontItems()

        val file = ClassGenerator(extension, itemMap).build()
        parser.getTTFUrl()?.let {
            val ttfFile = downloadFile(it)
            createFolderAndFile(extension, file, ttfFile!!.bytes())
        }
        return
    }

    fun createFolderAndFile(configuration: IconicGeneratorPluginExtension, file: FileSpec, ttfByteArray: ByteArray) {
        val fileGenerator = FileGenerator(configuration)
        val rootFolder = File("%s-font/".format(configuration.name.toLowerCase()))
        val mainFolder = File(rootFolder.path + "/src/main/")
        val fontFolder = File(mainFolder.path + "/assets/fonts")
        val resFolder = File(mainFolder.path + "/res/values")
        val codeFolder = File(mainFolder.path + "/java/com/mikepenz/%s_typeface_library".format(configuration.name.toLowerCase()))
        val codeFile = File(codeFolder.path + "/%s.kt".format(configuration.name))
        val ttfFile = File(fontFolder.path + "/%s-font-v%s.ttf".format(configuration.name.toLowerCase(), configuration.versionName))

        rootFolder.mkdirs()
        mainFolder.mkdirs()
        fontFolder.mkdirs()
        resFolder.mkdirs()
        codeFolder.mkdirs()

        fileGenerator.generateFile("template/build.gradle", rootFolder.path + "/build.gradle")
        fileGenerator.generateFile("template/consumer-proguard-rules.pro", rootFolder.path + "/consumer-proguard-rules.pro")
        fileGenerator.generateFile("template/gradle.properties", rootFolder.path + "/gradle.properties")
        fileGenerator.generateFile("template/src/main/AndroidManifest.xml", mainFolder.path + "/AndroidManifest.xml")
        fileGenerator.generateFile("template/src/main/res/values/font_addon.xml", resFolder.path + "/font_addon.xml")

        file.writeTo(System.out)
        codeFile.writeText(file.toString())
        ttfFile.writeBytes(ttfByteArray)
    }

    fun downloadFile(url: String): ResponseBody? {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Failed to download file: $response")
        }
        return response.body()
    }
}