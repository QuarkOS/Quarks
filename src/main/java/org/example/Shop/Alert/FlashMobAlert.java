package org.example.Shop.Alert;

import net.dv8tion.jda.api.EmbedBuilder;

public class FlashMobAlert extends Alert {
    public FlashMobAlert(EmbedBuilder alertMessage) {
        super(alertMessage);
    }

    @Override
    public void sendAlert() {
        System.out.println("Flash Mob Alert: " + alertMessage.build().getDescription());
    }
}
