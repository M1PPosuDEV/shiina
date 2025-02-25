package dev.osunolimits.plugins;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static <T> void registerService(Class<T> serviceClass, T serviceInstance) {
        services.put(serviceClass, serviceInstance);
    }

    public static <T> T getService(Class<T> serviceClass) {
        return serviceClass.cast(services.get(serviceClass));
    }
}
