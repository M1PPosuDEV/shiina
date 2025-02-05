package dev.osunolimits.routes.get.modular;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ModulePath {
    private String path;
    private ShiinaModule module;
    private boolean isDefault;
}
