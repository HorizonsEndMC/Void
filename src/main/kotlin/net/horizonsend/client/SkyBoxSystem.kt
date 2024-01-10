package net.horizonsend.client

import net.horizonsend.client.networking.packets.GetCurrentWorld.world
import net.minecraft.util.Identifier

enum class SkyBoxSystem (val identifier: Identifier) {
    STANDARD(Identifier("textures/environment/custom_end_sky.png")),
    REGULUS(Identifier("textures/environment/regulus_end_sky.png")),
    ASTERI(Identifier("textures/environment/asteri_end_sky.png")),
    ILIOS(Identifier("textures/environment/ilios_end_sky.png")),
    SIRIUS(Identifier("textures/environment/sirius_end_sky.png")),
    HORIZON(Identifier("textures/environment/horizon_end_sky.png")),
    TRENCH(Identifier("textures/environment/trench_end_sky.png")),
    AU(Identifier("textures/environment/au_end_sky.png"))
}
fun currentSkybox(): SkyBoxSystem{
    if(world.contains("standard")) return SkyBoxSystem.STANDARD
    if(world.contains("regulus")) return SkyBoxSystem.REGULUS
    if(world.contains("asteri")) return SkyBoxSystem.ASTERI
    if(world.contains("ilios")) return SkyBoxSystem.ILIOS
    if(world.contains("sirius")) return SkyBoxSystem.SIRIUS
    if(world.contains("horizon")) return SkyBoxSystem.HORIZON
    if(world.contains("trench")) return SkyBoxSystem.TRENCH
    if(world.contains("au")) return SkyBoxSystem.AU
    return SkyBoxSystem.STANDARD
}