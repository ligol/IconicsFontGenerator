package fr.ligol.iconics_generator

import com.helger.css.ECSSVersion
import com.helger.css.decl.CSSExpressionMemberTermSimple
import com.helger.css.decl.CSSExpressionMemberTermURI
import com.helger.css.decl.CSSSelectorSimpleMember
import com.helger.css.decl.CascadingStyleSheet
import com.helger.css.reader.CSSReader
import java.nio.charset.StandardCharsets

class CssParser(private val cssFile: String) {
    private val aCSS = CSSReader.readFromString(cssFile, StandardCharsets.UTF_8, ECSSVersion.CSS30)

    fun getFontItems(): Map<String, String> {
        val map = HashMap<String, String>()

        for (styleRule in aCSS!!.allStyleRules) {
            if (styleRule.hasSelectors()) {
                if (styleRule.allSelectors[0].hasMembers()) {
                    if (styleRule.allSelectors[0].allMembers[0] is CSSSelectorSimpleMember) {
                        val item = styleRule.allSelectors[0].allMembers[0] as CSSSelectorSimpleMember
                        if (item.value.startsWith(".icon")) {
                            if (styleRule.hasDeclarations() && styleRule.allDeclarations[0].expression.allMembers[0] is CSSExpressionMemberTermSimple) {
                                val expression = styleRule.allDeclarations[0].expression.allMembers[0] as CSSExpressionMemberTermSimple
                                map.put(item.value, expression.value)
                            }
                        }
                    }
                }
            }
        }
        for (c in map) {
            println(c.key)
        }
        return map
    }

    fun getTTFUrl(): String? {
        val fontRule = aCSS!!.allFontFaceRules[0]
        return fontRule.allDeclarations
                .filter { it.expression.memberCount != 0 }
                .flatMap { it.expression.allMembers }
                .filterIsInstance<CSSExpressionMemberTermURI>()
                .firstOrNull { it.uriString.contains(".ttf") }
                ?.uriString
    }
}