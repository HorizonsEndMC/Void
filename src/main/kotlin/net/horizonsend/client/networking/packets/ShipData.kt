@file:Suppress("ObjectPropertyName")

package net.horizonsend.client.networking.packets

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.horizonsend.client.networking.IonPacketHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.text.Text

object ShipData {
    val isPiloting get() = _isPiloting
    val name get() = _name
    val type get() = _type
    val targets get() = _targets
    val hull get() = _hull
    val gravwell get() = _gravwell
    val weaponset get() = _weaponset
    val regenEfficiency get() = _regenEfficiency
    val targetSpeed get() = _targetSpeed
    val speed get() = _speed
    val shieldPowerMode get() = _shieldPM
    val weaponPowerMode get() = _weaponPM
    val thrusterPowerMode get() = _thrusterPM

    private var _isPiloting = false
    private var _name = Text.of("")!!
    private var _type = Text.of("")!!
    private var _targets = mutableMapOf<String, String>()
    private var _hull = 0
    private var _gravwell = false
    private var _weaponset = ""
    private var _regenEfficiency = 0.0
    private var _targetSpeed = 0
    private var _speed = 0.0
    private var _shieldPM = 0.0
    private var _weaponPM = 0.0
    private var _thrusterPM = 0.0

    object ShipDataPacket : IonPacketHandler() {
        override val name = "ship_data"

        override fun s2c(
            client: MinecraftClient,
            handler: ClientPlayNetworkHandler,
            buf: PacketByteBuf,
            responseSender: PacketSender
        ) {
            println("packet gotten!!!!")
            _isPiloting = buf.readBoolean()
            if (!_isPiloting) return

            _name = buf.readText()
            _type = buf.readText()
            _targets = mutableMapOf<String, String>().apply {
                repeat(buf.readInt()) {
                    put(buf.readString(), buf.readString())
                }
            }


            _hull = buf.readInt()
            _gravwell = buf.readBoolean()
            _weaponset = buf.readString()
            _regenEfficiency = buf.readDouble()
            _targetSpeed = buf.readInt()
            _speed = buf.readDouble()

            _shieldPM = buf.readDouble()
            _weaponPM = buf.readDouble()
            _thrusterPM = buf.readDouble()
        }
    }
}