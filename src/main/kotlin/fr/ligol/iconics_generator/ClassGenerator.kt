package fr.ligol.iconics_generator

import com.squareup.kotlinpoet.*
import java.util.HashMap

class ClassGenerator(private val configuration: IconicGeneratorPluginExtension,
                     private val items : Map<String, String>) {
    fun build(): FileSpec {
        return FileSpec.builder(packageName, configuration.name)
                .addType(TypeSpec.classBuilder(configuration.name)
                        .primaryConstructor(FunSpec.constructorBuilder().build())
                        .superclass(itypefaceType)
                        .companionObject(createCompanionObject())
                        .addFunction(createGetCharactersFunction())
                        .addFunction(createGetIconsFunction())
                        .addFunction(createOverrideFunctionReturningDefaultString("mappingPrefix", configuration.code))
                        .addFunction(createOverrideFunctionReturningDefaultString("fontName", configuration.name))
                        .addFunction(createOverrideFunctionReturningDefaultString("version", configuration.versionName))
                        .addFunction(createOverrideFunctionReturningDefaultString("author", configuration.author))
                        .addFunction(createOverrideFunctionReturningDefaultString("url", configuration.url))
                        .addFunction(createOverrideFunctionReturningDefaultString("description", configuration.description))
                        .addFunction(createOverrideFunctionReturningDefaultString("license", configuration.license))
                        .addFunction(createOverrideFunctionReturningDefaultString("licenseUrl", configuration.licenseUrl))
                        .addFunction(createIconCountFunction())
                        .addFunction(createIconFunction())
                        .addFunction(createGetTypefaceFunction())
                        .addType(EnumGenerator(configuration, items).build())
                        .build())
                .build()
    }

    private fun createGetTypefaceFunction(): FunSpec {
        return FunSpec.builder("getTypeface")
                .addParameter("context", contextType)
                .returns(typefaceType.asNullable())
                .beginControlFlow("if (typeface == null)")
                .beginControlFlow("try")
                .addStatement("typeface = Typeface.createFromAsset(context.getAssets(), \"fonts/\" + TTF_FILE)")
                .nextControlFlow("catch (e: Exception)")
                .addStatement("return null")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return typeface")
                .build()
    }

    private fun createIconFunction(): FunSpec {
        return FunSpec.builder("getIcon")
                .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
                .addParameter("key", String::class)
                .returns(iiconType)
                .addCode("return Icon.valueOf(key)")
                .build()
    }

    private fun createIconCountFunction(): FunSpec {
        return FunSpec.builder("iconCount")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Int::class)
                .addStatement("return mChars!!.size")
                .build()
    }

    private fun createGetIconsFunction(): FunSpec {
        return FunSpec.builder("getIcons")
                .addModifiers(KModifier.OVERRIDE)
                .returns(stringCollectionType)
                .addStatement("val icons = LinkedList<String>()")
                .beginControlFlow("for (value in Icon.values())")
                .addStatement("icons.add(value.name)")
                .endControlFlow()
                .addStatement("return icons")
                .build()
    }

    private fun createGetCharactersFunction(): FunSpec {
        return FunSpec.builder("getCharacters")
                .addModifiers(KModifier.OVERRIDE)
                .returns(charHashMapType)
                .beginControlFlow("if (mChars == null)")
                .addStatement("val aChars = %T()", charHashMapType)
                .beginControlFlow("for (v in Icon.values())")
                .addStatement("aChars.put(v.name, v.character)")
                .endControlFlow()
                .addStatement("mChars = aChars")
                .endControlFlow()
                .addStatement("return mChars")
                .build()
    }

    private fun createCompanionObject() : TypeSpec {
        return TypeSpec.companionObjectBuilder()
                .addProperty(PropertySpec.builder("TTF_FILE", String::class)
                        .initializer("%s-font-v%s.ttf".format(configuration.name.toLowerCase(), configuration.versionName))
                        .addModifiers(KModifier.CONST, KModifier.PRIVATE)
                        .build())
                .addProperty(PropertySpec.builder("typeface", typefaceType)
                        .mutable(true)
                        .initializer("null")
                        .addModifiers(KModifier.PRIVATE)
                        .build())
                .addProperty(PropertySpec.builder("mChars", String::class)
                        .mutable(true)
                        .initializer("null")
                        .addModifiers(KModifier.PRIVATE)
                        .build())
                .addProperty(PropertySpec.builder("typeface2", itypefaceType)
                        .mutable(true)
                        .initializer("null")
                        .addModifiers(KModifier.PRIVATE)
                        .build())
                .build()
    }

    private fun createOverrideFunctionReturningDefaultString(name: String, value: String): FunSpec {
        return FunSpec.builder(name)
                .addModifiers(KModifier.OVERRIDE)
                .returns(String::class)
                .addStatement("return %S", value)
                .build()
    }

    companion object {
        val packageName = "com.mikepenz.manakin_typeface_library"
        val charHashMapType = ParameterizedTypeName.get(HashMap::class, String::class, Character::class)
        val stringCollectionType = ParameterizedTypeName.get(Collection::class, String::class)
        val contextType = ClassName("android.content", "Context")
        val typefaceType = ClassName("android.graphics", "Typeface")
        val itypefaceType = ClassName("com.mikepenz.iconics.typeface", "ITypeface")
        val iiconType = ClassName("com.mikepenz.iconics.typeface", "IIcon")
    }
}