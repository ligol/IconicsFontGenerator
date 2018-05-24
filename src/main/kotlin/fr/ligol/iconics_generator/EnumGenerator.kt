package fr.ligol.iconics_generator

import com.squareup.kotlinpoet.*

class EnumGenerator(private val configuration: IconicGeneratorPluginExtension,
                    private val items : Map<String, String>) {

    fun build(): TypeSpec {
        val builder =  TypeSpec.enumBuilder("Icon")
                .addSuperinterface(iiconType)
                .addFunction(createGetCharacterFunction())
                .addFunction(createGetFormattedNameFunction())
                .addFunction(createGetNameFunction())
                .addFunction(createGetTypefaceFunction())
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addModifiers(KModifier.PRIVATE)
                        .addParameter("character", Char::class)
                        .build())

        createEnumValue(builder)
        return builder.build()
    }

    private fun createEnumValue(builder: TypeSpec.Builder) {
        for (item in items.entries) {
            val name = item.key.replace(".icon", configuration.code).replace("-", "_")
            val value = item.value.replace("\\e", "\\ue")

            builder.addEnumConstant(name, TypeSpec.anonymousClassBuilder(value)
                    .build())
        }
    }

    private fun createGetFormattedNameFunction(): FunSpec {
        return FunSpec.builder("getFormattedName")
                .addModifiers(KModifier.OVERRIDE)
                .returns(String::class)
                .addStatement("return \"{\$name}\"")
                .build()
    }

    private fun createGetCharacterFunction(): FunSpec {
        return FunSpec.builder("getCharacter")
                .addModifiers(KModifier.OVERRIDE)
                .returns(String::class)
                .addStatement("return character")
                .build()
    }

    private fun createGetNameFunction(): FunSpec {
        return FunSpec.builder("getName")
                .addModifiers(KModifier.OVERRIDE)
                .returns(String::class)
                .addStatement("return name")
                .build()
    }

    private fun createGetTypefaceFunction(): FunSpec {
        return FunSpec.builder("getTypeface")
                .addModifiers(KModifier.OVERRIDE)
                .returns(itypefaceType)
                .beginControlFlow("if (typeface2 == null)")
                .addStatement("typeface2 = %S()", configuration.name)
                .endControlFlow()
                .addStatement("return typeface2 as ITypeface")
                .build()
    }

    companion object {
        val iiconType = ClassName("com.mikepenz.iconics.typeface", "IIcon")
        val itypefaceType = ClassName("com.mikepenz.iconics.typeface", "ITypeface")
    }

}