package net.horizonsend.client.features

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.StickyKeyBinding
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import org.lwjgl.glfw.GLFW


object CratePlacer {
    fun init() {
        var ticksSensePlace = 0
        val keyBind = KeyBindingHelper.registerKeyBinding(StickyKeyBinding(
            "key.crateplacer.toggle",
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            "category.crateplacer"
        ) { true })

        UseBlockCallback.EVENT.register { playerEntity: PlayerEntity, world: World, hand: Hand, blockHitResult: BlockHitResult ->
            println("rclick ${blockHitResult.side}")
            println("rclick ${blockHitResult.blockPos}")
            println("rclick ${blockHitResult.pos}")
            println("rclick ${blockHitResult.isInsideBlock}")
            return@register ActionResult.SUCCESS
        }

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            ticksSensePlace++
            if (ticksSensePlace < 1) return@EndTick
            ticksSensePlace = 0

            // If there is no world we will crash if we try to do anything, check if there is a world or not.
            if (MinecraftClient.getInstance().world == null) {
                keyBind.isPressed = false
                return@EndTick
            }

            // If enabled
            if (keyBind.isPressed) {
                // Determine the size of the area that is in player reach
                val xmin: Int = (MinecraftClient.getInstance().player!!.x - 5).toInt()
                val xmax: Int = (MinecraftClient.getInstance().player!!.x + 5).toInt()
                val ymin: Int = (MinecraftClient.getInstance().player!!.y - 5).toInt()
                val ymax: Int = (MinecraftClient.getInstance().player!!.y + 5).toInt()
                val zmin: Int = (MinecraftClient.getInstance().player!!.z - 5).toInt()
                val zmax: Int = (MinecraftClient.getInstance().player!!.z + 5).toInt()

                // Loop though every block in player reach
                for (x in xmin..xmax) {
                    for (y in ymin..ymax) {
                        for (z in zmin..zmax) {

                            // If the block is a sticky piston
                            if (MinecraftClient.getInstance().world!!.getBlockState(BlockPos(x, y, z)).block
                                == Blocks.STICKY_PISTON
                            ) {
                                // Move to 9th Hotbar Slot
                                MinecraftClient.getInstance().player!!.inventory.selectedSlot = 8

                                // Figure out the block we need to place the shulker relative to the piston
                                val addPos: Vec3i =
                                    MinecraftClient.getInstance().world!!.getBlockState(BlockPos(x, y, z))
                                        .get(Properties.FACING).vector

                                // Determine crate position
                                val pos = Vec3d(
                                    (x + addPos.x).toDouble(),
                                    (y + addPos.y).toDouble(),
                                    (z + addPos.z).toDouble()
                                )
                                if (pos.isInRange(MinecraftClient.getInstance().player!!.pos, 5.0)) {
                                    // If there is not already a shulker there
                                    if (MinecraftClient.getInstance().world!!.getBlockState(
                                            BlockPos(
                                                Vec3i(
                                                    pos.x.toInt(),
                                                    pos.y.toInt(),
                                                    pos.z.toInt()
                                                )
                                            )
                                        ).block
                                        != BlockTags.SHULKER_BOXES
                                    ) {
                                        // If we are not holding a crate
                                        if (!MinecraftClient.getInstance().player!!.getStackInHand(Hand.MAIN_HAND)
                                                .item.translationKey.endsWith("shulker_box")
                                        ) {
                                            for (invSlot in 40 downTo 1) {
                                                if (MinecraftClient.getInstance().player!!.inventory.getStack(invSlot)
                                                        .item.translationKey.endsWith("shulker_box")
                                                ) {
                                                    MinecraftClient.getInstance().interactionManager!!.pickFromInventory(
                                                        invSlot
                                                    )
                                                    break
                                                }
                                            }
                                        }

                                        // Place the block
                                        val hitRes = BlockHitResult(
                                            pos,
                                            MinecraftClient.getInstance().world!!.getBlockState(BlockPos(x, y, z))
                                                .get(Properties.FACING),
                                            BlockPos(x, y, z),
                                            false
                                        )
                                        println("place ${hitRes.side}")
                                        println("place ${hitRes.blockPos}")
                                        println("place ${hitRes.pos}")
                                        println("place ${hitRes.isInsideBlock}")
                                        MinecraftClient.getInstance().interactionManager!!.interactBlock(
                                            MinecraftClient.getInstance().player,
                                            Hand.MAIN_HAND,
                                            hitRes
                                        )
                                        return@EndTick  // Don't do more then 1 per tick, its laggy and the server will hate you
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}