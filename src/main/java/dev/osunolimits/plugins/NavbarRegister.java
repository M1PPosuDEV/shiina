package dev.osunolimits.plugins;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import dev.osunolimits.plugins.models.NavbarItem;

public class NavbarRegister {

    private static final Logger log = (Logger) LoggerFactory.getLogger("NavbarRegister");
    private final static int START_ACTNAV = 20;
    private static List<NavbarItem> items = new ArrayList<>();

    public static void register(NavbarItem item) {
        log.info("Registering navbar item: '" + item.getName() + "' on Route (" + item.getUrl()+ ") with actNav (" + item.getActNav() + ")");
        items.add(item);
    }

    public static List<NavbarItem> getItems() {
        return items;
    }

    public static int getActNav() {
        return START_ACTNAV + items.size();
    }
    
}
