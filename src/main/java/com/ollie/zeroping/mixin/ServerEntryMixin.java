package com.ollie.zeroping.mixin;

import com.ollie.zeroping.ZeroPingConfig;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The multiplayer server list reads ServerInfo.ping (a public long field) every frame
 * inside ServerEntry.render. We overwrite it with a stable spoofed value once the
 * real ping has been received (ping >= 0).
 */
@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class ServerEntryMixin {
    @Shadow
    @Final
    private ServerInfo server;

    @Inject(method = "render", at = @At("HEAD"))
    private void zeroping$spoofPing(CallbackInfo ci) {
        ZeroPingConfig cfg = ZeroPingConfig.INSTANCE;
        if (cfg.isEnabled() && this.server != null && this.server.online && this.server.ping >= 0L) {
            this.server.ping = cfg.getStablePing(this.server);
        }
    }
}
