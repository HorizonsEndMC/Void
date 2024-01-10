package net.horizonsend.client.networking.packets

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.horizonsend.client.networking.IonPacketHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

object GetCurrentWorld {
    val world get() = _world

    private var _world: String = ""

    object GetCurrentWorld : IonPacketHandler() {
        override val name = "current_world"

        override fun s2c(
            client: MinecraftClient,
            handler: ClientPlayNetworkHandler,
            buf: PacketByteBuf,
            responseSender: PacketSender
        ) {
            println("packet gotten!!!!")

            _world = buf.readString()
        }
    }
}