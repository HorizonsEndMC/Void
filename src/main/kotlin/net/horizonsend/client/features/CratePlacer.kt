package net.horizonsend.client.features

import net.horizonsend.client.Void.isCratePlacerActive
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object CratePlacer {
    fun handleCratePlacer() {
        // Register Tick Action
        var ticksSensePlace = 0
        ticksSensePlace++
        if (ticksSensePlace < 1) return
        ticksSensePlace = 0

        // If enabled
        if (isCratePlacerActive) {
            // Determine the size of the area that is in player reach
            val xmin = MinecraftClient.getInstance().player!!.x.toInt() - 5
            val xmax = MinecraftClient.getInstance().player!!.x.toInt() + 5
            val ymin = MinecraftClient.getInstance().player!!.y.toInt() - 5
            val ymax = MinecraftClient.getInstance().player!!.y.toInt() + 5
            val zmin = MinecraftClient.getInstance().player!!.z.toInt() - 5
            val zmax = MinecraftClient.getInstance().player!!.z.toInt() + 5

            // Loop though every block in player reach
            for (x in xmin..xmax) {
                for (y in ymin..ymax) {
                    for (z in zmin..zmax) {

                        // If the block is a sticky piston
                        if (MinecraftClient.getInstance().world!!.getBlockState(
                                BlockPos(
                                    x,
                                    y,
                                    z
                                )
                            ).block == Blocks.STICKY_PISTON
                        ) {
                            // Move to 9th Hotbar Slot
                            MinecraftClient.getInstance().player!!.inventory.selectedSlot = 8

                            // Figure out the block we need to place the shulker relative to the piston
                            val addPos = MinecraftClient.getInstance().world!!.getBlockState(BlockPos(x, y, z)).get(
                                Properties.FACING
                            ).vector

                            // Determine crate position
                            val pos =
                                Vec3d((x + addPos.x).toDouble(), (y + addPos.y).toDouble(), (z + addPos.z).toDouble())
                            if (pos.isInRange(MinecraftClient.getInstance().player!!.pos, 5.0)) {
                                // If there is not already a shulker there
                                if (MinecraftClient.getInstance().world!!.getBlockState(
                                        BlockPos(
                                            pos.x.toInt(),
                                            pos.y.toInt(),
                                            pos.z.toInt()
                                        )
                                    ).block != BlockTags.SHULKER_BOXES
                                ) {
                                    // If we are not holding a crate
                                    if (!MinecraftClient.getInstance().player!!.getStackInHand(Hand.MAIN_HAND).item.translationKey.endsWith(
                                            "shulker_box"
                                        )
                                    ) {
                                        for (invSlot in 40 downTo 1) {
                                            if (MinecraftClient.getInstance().player!!.inventory.getStack(invSlot).item.translationKey.endsWith(
                                                    "shulker_box"
                                                )
                                            ) {
                                                MinecraftClient.getInstance().interactionManager!!.pickFromInventory(
                                                    invSlot
                                                )
                                                break
                                            }
                                        }
                                    }

                                    // Place the block
                                    MinecraftClient.getInstance().interactionManager!!.interactBlock(
                                        MinecraftClient.getInstance().player,
                                        Hand.MAIN_HAND,
                                        BlockHitResult(
                                            MinecraftClient.getInstance().player!!.pos,
                                            MinecraftClient.getInstance().world!!.getBlockState(BlockPos(x, y, z)).get(
                                                Properties.FACING
                                            ),
                                            BlockPos(x, y, z),
                                            false
                                        )
                                    )
                                    return  // Don't do more then 1 per tick, its laggy and the server will hate you
                                }
                            }
                        }
                    }
                }
            }


            // Finish
            println("Crateplacer Started")
        }
    }
}
