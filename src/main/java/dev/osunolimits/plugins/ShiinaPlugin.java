package dev.osunolimits.plugins;

import ch.qos.logback.classic.Logger;

public abstract class ShiinaPlugin {

    protected abstract void onEnable(String pluginName, Logger logger);

    protected abstract void onDisable(String pluginName, Logger logger);
}
