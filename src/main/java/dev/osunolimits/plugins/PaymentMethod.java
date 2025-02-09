package dev.osunolimits.plugins;

public abstract class PaymentMethod {
    public abstract String getMethodName();
    public abstract boolean hasIncludedImage();
    public abstract void onEnable();
    public abstract void onDisable();
}
