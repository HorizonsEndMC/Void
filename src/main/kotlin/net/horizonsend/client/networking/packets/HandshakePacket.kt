package net.horizonsend.client.networking.packets

import me.shedaniel.rei.api.common.plugins.PluginManager
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.horizonsend.client.Void
import net.horizonsend.client.features.ReiIntegration
import net.horizonsend.client.networking.IonPacketHandler
import net.horizonsend.client.networking.Packets
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

object HandshakePacket : IonPacketHandler() {
    override val name = "handshake"
    override fun s2c(
        client: MinecraftClient,
        handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
        Packets.HANDSHAKE.send()
        if (Void.reiExists) {
            ReiIntegration.items.clear()

            for (i in 1..buf.readInt()) {
                ReiIntegration.items.add(buf.readItemStack())
            }

            Thread {
                while (PluginManager.getInstance().isReloading) {
                    Thread.sleep(1)
                }

                PluginManager.getInstance().startReload()
            }.start()
        }
    }
}

