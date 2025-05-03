package dev.osunolimits.modules;

import dev.osunolimits.models.Group;

public class ShiinaSupporterBadge {
    private static final ShiinaSupporterBadge instance = new ShiinaSupporterBadge();

    private String emoji;
    private String name;
    private String description;


    public ShiinaSupporterBadge() {
        this.emoji =XmlConfig.getInstance().getOrDefault("donator.badge.emoji", "🌟");
        this.name = XmlConfig.getInstance().getOrDefault("donator.badge.name", "Supporter");
        this.description = XmlConfig.getInstance().getOrDefault("donator.badge.description", "Supporter");
    }

    public static ShiinaSupporterBadge getInstance() {
        return instance;
    }

    public Group getGroup() {
        return new Group(name, emoji, description);
    }
}
