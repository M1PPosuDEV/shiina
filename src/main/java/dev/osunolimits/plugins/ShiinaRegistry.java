package dev.osunolimits.plugins;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class ShiinaRegistry {
private static Logger log = (Logger) LoggerFactory.getLogger("ShiinaRegistry");
    private static List<ShiinaEventListener> listeners = new ArrayList<>();

    public static void registerListener(ShiinaEventListener listener) {
        listeners.add(listener);
        log.info("Registered listener: " + listener.getClass().getSimpleName());
    }

    public static void unregisterListener(ShiinaEventListener listener) {
        listeners.remove(listener);
        log.info("Unregistered listener: " + listener.getClass().getSimpleName());
    }

    public static List<ShiinaEventListener> getListeners() {
        return listeners;
    }

    
}
