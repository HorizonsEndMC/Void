package net.horizonsend.client.features

import me.x150.renderer.event.RenderEvents
import me.x150.renderer.render.Renderer2d
import net.horizonsend.client.networking.packets.ShipData
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import java.awt.Color

object ShipStatusDisplay {
    fun init() {
        RenderEvents.HUD.register {
            if (!ShipData.isPiloting) return@register

            val x = 10.0
            val y = 5.0

            val renderer = MinecraftClient.getInstance().textRenderer
            val m4f = it.matrices.peek().positionMatrix
            val data = mutableMapOf<String, Any>(
                "Gravity Well" to if (ShipData.gravwell) "§aON" else "§cOFF",
                "Shield Regen Efficiency" to ShipData.regenEfficiency,
                "Weapon Set" to ShipData.weaponset,
                "Cruise" to "${ShipData.speed}/${ShipData.targetSpeed}",
                "Hull" to ShipData.hull
            )

            var longestText = 0
            var starting = 0

            fun increment() {
                starting +=
                    if (starting == 0) renderer.fontHeight
                    else 5 + renderer.fontHeight
            }

            fun drawText(s: String) {
                increment()

                val textSize = renderer.getWidth(s)
                if (longestText < textSize) longestText = textSize

                renderer.draw(
                    s,
                    x.toFloat() + renderer.getWidth(s), y.toFloat() + starting,
                    Color.WHITE.rgb, true, m4f, it.vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880
                )
            }

            drawText("Name: ")
            renderer.draw(
                ShipData.name,
                x.toFloat() + renderer.getWidth("Name: "), y.toFloat() + starting,
                Color.WHITE.rgb, true, m4f, it.vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880
            )

            drawText("Type: ")
            renderer.draw(
                ShipData.type,
                x.toFloat() + renderer.getWidth("Type: "), y.toFloat() + starting,
                Color.WHITE.rgb, true, m4f, it.vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880
            )

            for ((name, value) in data) {
                drawText("$name: $value")
            }

            increment()

            drawText("Turret Targets")
            for ((name, target) in ShipData.targets) {
                drawText(" • $name: $target")
            }

            increment()

            drawText("Power Modes")
            val sum = ShipData.shieldPowerMode + ShipData.weaponPowerMode + ShipData.thrusterPowerMode
            drawText(" • Shield: ${(ShipData.shieldPowerMode / sum * 100.0).toInt()}")
            drawText(" • Weapon: ${(ShipData.weaponPowerMode / sum * 100.0).toInt()}")
            drawText(" • Thruster: ${(ShipData.thrusterPowerMode / sum * 100.0).toInt()}")

            increment()

            Renderer2d.renderRoundedOutline(
                it.matrices,
                chromaColor(),
                x - 5, y,
                x + longestText + 5, y + starting + 2,
                10f, 1f, 10f
            )
        }
    }

    fun chromaColor() =
        Color.getHSBColor((System.currentTimeMillis() % 2000f) / 1000f, 0.8f, 0.8f)
}