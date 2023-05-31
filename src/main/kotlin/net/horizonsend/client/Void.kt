package net.horizonsend.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.loader.api.FabricLoader
import net.horizonsend.client.features.CratePlacer
import net.horizonsend.client.features.ReiIntegration
import net.horizonsend.client.features.ShipStatusDisplay
import net.horizonsend.client.networking.Packets
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import kotlin.properties.Delegates


val mc get() = MinecraftClient.getInstance()

@Environment(EnvType.CLIENT)
@Suppress("Unused")
object Void : ClientModInitializer {
    var reiExists by Delegates.notNull<Boolean>()
    override fun onInitializeClient() {
        CratePlacer.init()
        reiExists = FabricLoader.getInstance().isModLoaded("roughlyenoughitems")

        for (packet in Packets.values()) {
            println("Registering packet ${packet.handler.id}.")
            ClientPlayNetworking.registerGlobalReceiver(packet.handler.id) { m: MinecraftClient, c: ClientPlayNetworkHandler, p: PacketByteBuf, ps: PacketSender ->
                println("Received ${packet.handler.id}")
                packet.handler.s2c(m, c, p, ps)
            }
        }

        ShipStatusDisplay.init()
        DISCONNECT.register { _, _ -> ReiIntegration.items.clear() }
    }
}

fun id(s: String) = Identifier("ion", s)