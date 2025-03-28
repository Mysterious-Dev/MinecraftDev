/*
 * Minecraft Development for IntelliJ
 *
 * https://mcdev.io/
 *
 * Copyright (C) 2023 minecraft-dev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3.0 only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.demonwav.mcdev.translations.identification

import com.demonwav.mcdev.translations.identification.TranslationInstance.Companion.FormattingError
import com.demonwav.mcdev.translations.index.TranslationIndex
import com.demonwav.mcdev.translations.index.merge
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiCallExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiExpressionList
import java.util.MissingFormatArgumentException

abstract class TranslationIdentifier<T : PsiElement> {
    @Suppress("UNCHECKED_CAST")
    fun identifyUnsafe(element: PsiElement): TranslationInstance? {
        return identify(element as T)
    }

    abstract fun identify(element: T): TranslationInstance?

    abstract fun elementClass(): Class<T>

    companion object {
        val INSTANCES = listOf(LiteralTranslationIdentifier(), ReferenceTranslationIdentifier())

        fun identify(
            project: Project,
            element: PsiExpression,
            container: PsiElement,
            referenceElement: PsiElement,
        ): TranslationInstance? {
            if (container is PsiExpressionList && container.parent is PsiCallExpression) {
                val call = container.parent as PsiCallExpression
                val index = container.expressions.indexOf(element)

                for (function in TranslationInstance.translationFunctions) {
                    if (function.matches(call, index)) {
                        val translationKey = function.getTranslationKey(call, referenceElement) ?: continue
                        val entries = TranslationIndex.getAllDefaultEntries(project).merge("")
                        val translation = entries[translationKey.full]?.text
                        if (translation != null) {
                            val foldingElement = when (function.foldParameters) {
                                TranslationFunction.FoldingScope.CALL -> call
                                TranslationFunction.FoldingScope.PARAMETER -> element
                                TranslationFunction.FoldingScope.PARAMETERS -> container
                            }
                            try {
                                val (formatted, superfluousParams) = function.format(translation, call)
                                    ?: (translation to -1)
                                return TranslationInstance(
                                    foldingElement,
                                    function.matchedIndex,
                                    referenceElement,
                                    translationKey,
                                    formatted,
                                    if (superfluousParams >= 0) FormattingError.SUPERFLUOUS else null,
                                    superfluousParams,
                                )
                            } catch (ignored: MissingFormatArgumentException) {
                                return TranslationInstance(
                                    foldingElement,
                                    function.matchedIndex,
                                    referenceElement,
                                    translationKey,
                                    translation,
                                    FormattingError.MISSING,
                                )
                            }
                        } else {
                            return TranslationInstance(
                                null,
                                function.matchedIndex,
                                referenceElement,
                                translationKey,
                                null,
                            )
                        }
                    }
                }
                return null
            }
            return null
        }
    }
}
