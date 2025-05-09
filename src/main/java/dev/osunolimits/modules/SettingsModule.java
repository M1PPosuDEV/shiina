package dev.osunolimits.modules;

import lombok.Data;

public class SettingsModule {
    
    public enum SettingType {
        STRING,
        INTEGER,
        BOOLEAN,
        DOUBLE
    }

    @Data
    public class Setting {
        private final String key;
        private final SettingType type;
        private final Object defaultValue;   
    }

    private static final SettingsModule instance = new SettingsModule();

    public void RegisterSetting(String category, Setting setting) {
        
    }
}
