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

package com.demonwav.mcdev.platform.bungeecord.framework

import com.demonwav.mcdev.asset.PlatformAssets
import com.demonwav.mcdev.facet.MavenLibraryPresentationProvider
import com.intellij.framework.library.LibraryVersionProperties

class BungeeCordPresentationProvider :
    MavenLibraryPresentationProvider(BUNGEECORD_LIBRARY_KIND, "net.md-5", "bungeecord-api") {
    override fun getIcon(properties: LibraryVersionProperties?) = PlatformAssets.BUNGEECORD_ICON
}

class WaterfallPresentationProvider :
    MavenLibraryPresentationProvider(WATERFALL_LIBRARY_KIND, "io.github.waterfallmc", "waterfall-api") {
    override fun getIcon(properties: LibraryVersionProperties?) = PlatformAssets.WATERFALL_ICON
}
