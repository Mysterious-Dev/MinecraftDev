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

package com.demonwav.mcdev.toml

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.toml.lang.psi.TomlElementTypes
import org.toml.lang.psi.TomlKeyValue
import org.toml.lang.psi.TomlLiteral
import org.toml.lang.psi.TomlValue
import org.toml.lang.psi.ext.elementType

/** Inserts `=` between key and value if missed and wraps inserted string with quotes if needed */
class TomlStringValueInsertionHandler(private val keyValue: TomlKeyValue) : InsertHandler<LookupElement> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        var startOffset = context.startOffset
        val value = context.getElementOfType<TomlValue>()
        val hasEq = keyValue.children.any { it.elementType == TomlElementTypes.EQ }
        val hasQuotes =
            value != null && (value !is TomlLiteral || value.firstChild.elementType != TomlElementTypes.NUMBER)

        if (!hasEq) {
            context.document.insertString(startOffset - if (hasQuotes) 1 else 0, "= ")
            PsiDocumentManager.getInstance(context.project).commitDocument(context.document)
            startOffset += 2
        }

        if (!hasQuotes) {
            context.document.insertString(startOffset, "\"")
            context.document.insertString(context.selectionEndOffset, "\"")
        }
    }
}

inline fun <reified T : PsiElement> InsertionContext.getElementOfType(strict: Boolean = false): T? =
    PsiTreeUtil.findElementOfClassAtOffset(file, tailOffset - 1, T::class.java, strict)
