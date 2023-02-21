package net.horizonsend.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.loader.api.FabricLoader
import net.horizonsend.client.features.ReiIntegration
import net.horizonsend.client.networking.Packets
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import kotlin.properties.Delegates

@Environment(EnvType.CLIENT)
@Suppress("Unused")
object Void : ClientModInitializer {
	var reiExists by Delegates.notNull<Boolean>()

	override fun onInitializeClient() {
		reiExists = FabricLoader.getInstance().isModLoaded("roughlyenoughitems")

		for (packet in Packets.values().filterNot { it.s2c == null }) {
			println("Registering packet ${packet.id}.")
			ClientPlayNetworking.registerGlobalReceiver(packet.id) { minecraftClient: MinecraftClient, clientPlayNetworkHandler: ClientPlayNetworkHandler, packetByteBuf: PacketByteBuf, packetSender: PacketSender ->
				println("Received ${packet.id}")
				packet.s2c!!.receive(minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender)
			}
		}

		DISCONNECT.register { _, _ -> ReiIntegration.items.clear() }
	}
}