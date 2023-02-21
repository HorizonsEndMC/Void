package code.integrations

import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry
import me.shedaniel.rei.api.common.plugins.PluginManager
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf

class ReiIntegration: REIClientPlugin {
    companion object {
        val items = mutableListOf<ItemStack>()
        fun handle(buf: PacketByteBuf) {
            println("loading REI integration")
            items.clear()

            for (i in 1..buf.readInt()) {
                items.add(buf.readItemStack())
            }

            PluginManager.getInstance().startReload()
        }
    }

    override fun registerEntries(registry: EntryRegistry?) {
        EntryRegistry.getInstance().addEntries(items.map { EntryStacks.of(it) })
    }
}