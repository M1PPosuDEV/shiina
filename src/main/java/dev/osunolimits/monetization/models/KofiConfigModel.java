package dev.osunolimits.monetization.models;

import lombok.Data;

@Data
public class KofiConfigModel {
    private String verificationToken = "";
    private String pageName = "";
    private double donationAmount = 5.0;
}
