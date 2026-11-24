package com.ollie.zeroping;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

public final class ZeroPingConfig {
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("zero-ping.properties");

    private volatile int minPing = 0;
    private volatile int maxPing = 5;
    private volatile boolean enabled = true;

    // Keeps the displayed value stable per object so the number doesn't flicker every frame.
    private final Map<Object, Integer> stable = Collections.synchronizedMap(new WeakHashMap<>());

    public static final ZeroPingConfig INSTANCE = new ZeroPingConfig();

    private ZeroPingConfig() {
        load();
    }

    public void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }
        Properties props = new Properties();
        try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
            props.load(reader);
            this.enabled = Boolean.parseBoolean(props.getProperty("enabled", "true"));
            this.minPing = clamp(readInt(props, "min-ping", 0));
            this.maxPing = clamp(readInt(props, "max-ping", 5));
            stable.clear();
        } catch (IOException e) {
            System.err.println("[zero-ping] Config read failed, using defaults: " + e);
        }
    }

    private static int readInt(Properties props, String key, int fallback) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(fallback)).trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(999, value));
    }

    public void save() {
        Properties props = new Properties();
        props.setProperty("enabled", String.valueOf(enabled));
        props.setProperty("min-ping", String.valueOf(minPing));
        props.setProperty("max-ping", String.valueOf(maxPing));
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (var writer = Files.newBufferedWriter(CONFIG_PATH)) {
                props.store(writer, "zero-ping config");
            }
        } catch (IOException e) {
            System.err.println("[zero-ping] Config write failed: " + e);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getMin() {
        return minPing;
    }

    public int getMax() {
        return maxPing;
    }

    /** A random ping in [min, max]. */
    public int getDisplayPing() {
        int lo = minPing;
        int hi = maxPing;
        if (hi <= lo) {
            return lo;
        }
        return lo + (int) (Math.random() * (hi - lo + 1));
    }

    /** A random ping in [min, max] that stays the same for the given key (e.g. a server entry). */
    public int getStablePing(Object key) {
        return stable.computeIfAbsent(key, k -> getDisplayPing());
    }
}
