package dev.osunolimits.plugins.models;

import dev.osunolimits.plugins.NavbarRegister;
import lombok.Data;

@Data
public class NavbarItem {
    private String name;
    private String url;
    private int actNav;

    public NavbarItem(String name, String url) {
        this.name = name;
        this.url = url;
        actNav = NavbarRegister.getActNav();
    }
}