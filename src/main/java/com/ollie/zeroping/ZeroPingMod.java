package com.ollie.zeroping;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ZeroPingMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("zero-ping");

    @Override
    public void onInitializeClient() {
        ZeroPingConfig cfg = ZeroPingConfig.INSTANCE;
        cfg.load();
        LOGGER.info("Zero Ping loaded — display range {}-{} ms (enabled: {})",
                cfg.getMin(), cfg.getMax(), cfg.isEnabled());
    }
}
