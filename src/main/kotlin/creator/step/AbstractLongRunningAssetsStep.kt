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

package com.demonwav.mcdev.creator.step

import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.project.Project

abstract class AbstractLongRunningAssetsStep(parent: NewProjectWizardStep) : AbstractLongRunningStep(parent) {
    protected val assets = object : FixedAssetsNewProjectWizardStep(parent) {
        override fun setupAssets(project: Project) {
            outputDirectory = context.projectFileDirectory
            this@AbstractLongRunningAssetsStep.setupAssets(project)
        }
    }

    abstract fun setupAssets(project: Project)

    override fun perform(project: Project) {
        assets.setupProject(project)
    }
}
