package net.horizonsend.client.mixins;

import net.horizonsend.client.Caches;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListMixin {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void injected(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (Caches.INSTANCE.getModUsers().contains(entry.getProfile().getId())) {
            cir.setReturnValue(
                    MutableText.of(new LiteralTextContent("✔ "))
                            .formatted(Formatting.GREEN)
                            .append(cir.getReturnValue())
            );
        }
    }
}
