package net.horizonsend.client.networking

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.horizonsend.client.id
import net.horizonsend.client.networking.packets.HandshakePacket
import net.horizonsend.client.networking.packets.PlayerAdd
import net.horizonsend.client.networking.packets.PlayerRemove
import net.horizonsend.client.networking.packets.ShipData
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

abstract class IonPacketHandler {
    abstract val name: String
    val id by lazy { id(name.lowercase()) }

    open fun s2c(
        client: MinecraftClient,
        handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
    }

    open fun c2s(buf: PacketByteBuf) {}
}

enum class Packets(
    val handler: IonPacketHandler
) {
    HANDSHAKE(HandshakePacket),
    PLAYER_ADD(PlayerAdd),
    SHIP_DATA(ShipData.ShipDataPacket),
    PLAYER_REMOVE(PlayerRemove);

    fun send() {
        ClientPlayNetworking.send(handler.id, PacketByteBufs.create().apply { handler.c2s(this) })
    }
}