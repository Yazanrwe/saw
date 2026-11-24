package com.ollie.zeroping.mixin;

import com.ollie.zeroping.ZeroPingConfig;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The tab list draws its latency bars from PlayerListEntry.getLatency().
 */
@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    @Inject(method = "getLatency", at = @At("HEAD"), cancellable = true)
    private void zeroping$spoofLatency(CallbackInfoReturnable<Integer> cir) {
        ZeroPingConfig cfg = ZeroPingConfig.INSTANCE;
        if (cfg.isEnabled()) {
            cir.setReturnValue(cfg.getStablePing(this));
        }
    }
}
