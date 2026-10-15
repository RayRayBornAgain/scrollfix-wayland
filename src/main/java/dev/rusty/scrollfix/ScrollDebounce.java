package dev.rusty.scrollfix;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Properties;

public final class ScrollDebounce {
    private static final long THRESHOLD_NS;
    private static long lastNs = 0;
    private static int lastSign = 0;
    private static boolean announced = false;

    static {
        long ms = 25;
        Path cfg = Paths.get("config", "scrollfix-wayland.properties");
        try {
            Properties p = new Properties();
            if (Files.exists(cfg)) {
                try (InputStream in = Files.newInputStream(cfg)) { p.load(in); }
                ms = Long.parseLong(p.getProperty("thresholdMs", "25").trim());
            } else {
                Files.createDirectories(cfg.getParent());
                p.setProperty("thresholdMs", "25");
                try (OutputStream out = Files.newOutputStream(cfg)) {
                    p.store(out, "Scroll events in the same direction closer together than this (milliseconds) are treated as duplicates and ignored.\n"
                        + "Raise it if the hotbar still skips; lower it if fast scrolling feels like it drops notches.");
                }
            }
        } catch (Exception e) {
            System.out.println("[ScrollFix Wayland] Could not read config, using 25 ms: " + e);
        }
        THRESHOLD_NS = Math.max(0, ms) * 1_000_000L;
        System.out.println("[ScrollFix Wayland] Loaded, duplicate window = " + ms + " ms");
    }

    private ScrollDebounce() {}

    /** Returns true if this scroll event should be dropped as a duplicate. */
    public static boolean shouldDrop(double yOffset) {
        if (yOffset == 0) return false;
        long now = System.nanoTime();
        int sign = yOffset > 0 ? 1 : -1;
        if (sign == lastSign && now - lastNs < THRESHOLD_NS) {
            if (!announced) {
                announced = true;
                System.out.println("[ScrollFix Wayland] Dropped a duplicate scroll event (fix is working)");
            }
            return true;
        }
        lastNs = now;
        lastSign = sign;
        return false;
    }
}
