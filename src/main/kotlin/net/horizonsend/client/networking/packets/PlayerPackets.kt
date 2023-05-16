package net.horizonsend.client.networking.packets

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.horizonsend.client.Caches
import net.horizonsend.client.networking.IonPacketHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

object PlayerAdd : IonPacketHandler() {
    override val name = "player_add"
    override fun s2c(
        client: MinecraftClient,
        handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
        Caches.modUsers.add(buf.readUuid())
    }
}

object PlayerRemove : IonPacketHandler() {
    override val name = "player_remove"
    override fun s2c(
        client: MinecraftClient,
        handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
        Caches.modUsers.remove(buf.readUuid())
    }
}