package code

import code.integrations.ReiIntegration
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class VoidClient : ClientModInitializer {
    override fun onInitializeClient() {
        println("init")
        val reiExists = FabricLoader.getInstance().isModLoaded("roughlyenoughitems")

        ClientPlayNetworking.registerGlobalReceiver(id("register")) { _: MinecraftClient, _: ClientPlayNetworkHandler, buf: PacketByteBuf, _: PacketSender ->
            println("got msg")
            if (reiExists)
                ReiIntegration.handle(buf)
        }
    }

    companion object {
        fun id(s: String) = Identifier("ion", s)
    }
}