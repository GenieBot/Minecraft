package io.sponges.bot.modules.minecraft.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HasPaidCommand extends Command {

    private static final String REQUEST_URL = "https://api.mojang.com/users/profiles/minecraft/";

    public HasPaidCommand() {
        super("checks if a minecraft name is a paid account", "haspaid", "ispremium", "ispaid", "mcname", "mchaspaid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: haspaid [username]");
            return;
        }
        String username = args[0];
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(REQUEST_URL + username).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            request.reply("Something went wrong :(");
            return;
        }
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 SpongyBot");
        connection.addRequestProperty("X-Request-From-SpongyBot", "client=" + request.getClient().getId() + "network="
                + request.getNetwork().getId() + "channel=" + request.getChannel().getId() + "user="
                + request.getUser().getId());
        boolean premium;
        try {
            premium = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            request.reply("Something went wrong: " + e.getMessage());
            return;
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String response;
            while ((response = reader.readLine()) != null) {
                builder.append(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            request.reply("Something went wrong :(");
            return;
        }
        request.reply(premium ? username + " is a premium minecraft account!" : username + " is not a premium minecraft account!");
    }
}