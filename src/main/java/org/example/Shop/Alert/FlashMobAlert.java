package org.example.Shop.Alert;

import net.dv8tion.jda.api.EmbedBuilder;

public class FlashMobAlert extends Alert {
    public FlashMobAlert(EmbedBuilder alertMessage) {
        super(alertMessage);
    }

    @Override
    public void sendAlert() {
        alertMessage.setTitle("Flash Mob Alert!")
                .setDescription("A flash mob is happening right now!")
                .setColor(0xff0000) // Red color
                .addField("Location", "Main Square", false)
                .addField("Time", "5:00 PM", false)
                .setFooter("Be there or be square!");

        System.out.println("Flash Mob Alert: " + alertMessage.build().getDescription());
    }
}
