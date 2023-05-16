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

package com.demonwav.mcdev.nbt.editor

import com.demonwav.mcdev.nbt.NbtVirtualFile
import com.demonwav.mcdev.util.runWriteTaskLater
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComboBox

class NbtToolbar(nbtFile: NbtVirtualFile) {
    private lateinit var compressionBox: Cell<JComboBox<CompressionSelection>>

    val panel = panel {
        row {
            label("File Type:")
            compressionBox = comboBox(listOf(CompressionSelection.GZIP, CompressionSelection.UNCOMPRESSED))
            button("Save") {
                runWriteTaskLater {
                    nbtFile.writeFile(this)
                }
            }
        }
    }

    //private var lastSelection: CompressionSelection

    init {
        /*compressionBox.selectedItem =
            if (nbtFile.isCompressed) CompressionSelection.GZIP else CompressionSelection.UNCOMPRESSED*/
        //lastSelection = selection

        if (!nbtFile.isWritable || !nbtFile.parseSuccessful) {
            compressionBox.enabled(false)
        }

        if (!nbtFile.parseSuccessful) {
            panel.isVisible = false
        }

        /*saveButton.addActionListener {
            lastSelection = selection

            runWriteTaskLater {
                nbtFile.writeFile(this)
            }
        }*/
    }

    val selection
        get() = compressionBox.item as CompressionSelection
}
