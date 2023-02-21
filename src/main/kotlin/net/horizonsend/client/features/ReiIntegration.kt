package net.horizonsend.client.features

import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.item.ItemStack

class ReiIntegration : REIClientPlugin {
	companion object {
		val items = mutableListOf<ItemStack>()
	}

	override fun registerEntries(registry: EntryRegistry?) {
		println("Adding ${items.size} items to REI")
		EntryRegistry.getInstance().addEntries(items.map { EntryStacks.of(it) })
	}
}