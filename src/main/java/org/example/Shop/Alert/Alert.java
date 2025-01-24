package org.example.Shop.Alert;

import net.dv8tion.jda.api.EmbedBuilder;

public abstract class Alert {
    public abstract void sendAlert();
    public EmbedBuilder alertMessage;

    public Alert(EmbedBuilder alertMessage) {
        this.alertMessage = alertMessage;
    }
}
