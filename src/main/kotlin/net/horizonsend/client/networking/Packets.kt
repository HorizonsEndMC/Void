package net.horizonsend.client.networking

import io.netty.buffer.Unpooled
import me.shedaniel.rei.api.common.plugins.PluginManager
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.horizonsend.client.Void
import net.horizonsend.client.Caches
import net.horizonsend.client.features.ReiIntegration
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket
import net.minecraft.util.Identifier

enum class Packets(
    val s2c: ClientPlayNetworking.PlayChannelHandler? = null,
    private val c2s: (PacketByteBuf.() -> Unit)? = null
) {
    HANDSHAKE(
        s2c = { _, _, buf: PacketByteBuf, _ ->
            HANDSHAKE.send()
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
        }, c2s = {}
    ),
    PLAYER_ADD(
        s2c = { _, _, buf: PacketByteBuf, _ ->
            Caches.modUsers.add(buf.readUuid())
        }
    ),
    PLAYER_REMOVE(
        s2c = { _, _, buf: PacketByteBuf, _ ->
            Caches.modUsers.remove(buf.readUuid())
        }
    );

    val id by lazy { id(name.lowercase()) }

    fun send(add: (PacketByteBuf.() -> Unit)? = null) {
        check(c2s != null) { "Packet is S2C only" }

        println("Sending $id")
        MinecraftClient.getInstance().player?.networkHandler?.sendPacket(
            CustomPayloadC2SPacket(
                id,
                PacketByteBuf(Unpooled.buffer()).apply {
                    c2s.invoke(this)
                    add?.invoke(this)
                }
            )
        )
    }

    companion object {
        fun id(s: String) = Identifier("ion", s)
    }
}