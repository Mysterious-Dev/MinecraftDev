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

package com.demonwav.mcdev.platform.mixin.implements

import com.demonwav.mcdev.framework.EdtInterceptor
import com.demonwav.mcdev.platform.mixin.BaseMixinTest
import com.demonwav.mcdev.platform.mixin.inspection.implements.InterfaceIsInterfaceInspection
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(EdtInterceptor::class)
@DisplayName("@Interface Is Interface Inspection Tests")
class InterfaceIsInterfaceTest : BaseMixinTest() {

    @BeforeEach
    fun setupProject() {
        buildProject {
            dir("test") {
                java(
                    "DummyFace.java",
                    """
                    package test;

                    interface DummyFace {

                    }
                    """,
                    configure = false,
                )

                java(
                    "DummyClass.java",
                    """
                    package test;

                    class DummyClass {

                    }
                    """,
                    configure = false,
                )

                java(
                    "InterfaceIsInterfaceMixin.java",
                    """
                    package test;

                    import org.spongepowered.asm.mixin.Mixin;
                    import org.spongepowered.asm.mixin.Implements;
                    import org.spongepowered.asm.mixin.Interface;

                    @Mixin
                    @Implements({
                        @Interface(iface = DummyFace.class, prefix = "good$"),
                        @Interface(iface = <error descr="Interface expected here">DummyClass.class</error>, prefix = "bad$"),
                    })
                    class InterfaceIsInterfaceMixin {

                    }
                    """,
                )
            }
        }
    }

    @Test
    @DisplayName("Highlight On @Interface Test")
    fun highlightOnInterfaceTest() {
        fixture.enableInspections(InterfaceIsInterfaceInspection::class)
        fixture.checkHighlighting(true, false, false)
    }
}
