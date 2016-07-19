package io.sponges.bot.modules.minecraft.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MCStatusCommand extends Command {

    private static final String REQUEST_URL = "http://status.mojang.com/check?service=";

    public MCStatusCommand() {
        super("shows mojang server status'", "mcstatus", "mojangstatus", "minecraftstatus", "xpaw");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        StringBuilder builder = new StringBuilder("Mojang server statuses:");
        int added = 0;
        for (Service service : Service.values()) {
            Status status = getStatus(service, request);
            if (status != null && status == Status.GREEN) {
                continue;
            }
            builder.append("\n").append(service.getFormattedName()).append(": ")
                    .append(status == null ? "Couldn't check status" : status.value.toLowerCase());
            added++;
        }
        if (added > 0) request.reply(builder.toString());
        else request.reply("Mojang server statuses: 100% online");
    }

    private Status getStatus(Service service, CommandRequest request) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(REQUEST_URL + service.url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 SpongyBot");
        connection.addRequestProperty("X-Request-From-SpongyBot", "client=" + request.getClient().getId() + "network="
                + request.getNetwork().getId() + "channel=" + request.getChannel().getId() + "user="
                + request.getUser().getId());
        String input;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            input = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONObject json = new JSONObject(input);
        Status status = Status.valueOf(json.getString(service.url).toUpperCase());
        if (status == null) return null;
        else return status;
    }

    private enum Status {

        GREEN("online"), YELLOW("slow"), RED("offline");

        private final String value;

        Status(String value) {
            this.value = value;
        }

    }

    private enum Service {

        MINECRAFT_WEBSITE("minecraft.net"),
        MINECRAFT_SESSIONS("session.minecraft.net"),
        MINECRAFT_SKINS("skins.minecraft.net"),
        MINECRAFT_TEXTURES("textures.minecraft.net"),
        MOJANG_ACCOUNTS("account.mojang.com"),
        MOJANG_LOGINS("auth.mojang.com"),
        MOJANG_SESSIONS("sessionserver.mojang.com"),
        MOJANG_API("api.mojang.com");

        private final String url;

        Service(String url) {
            this.url = url;
        }

        String getFormattedName() {
            String formatted = name().toLowerCase().replace("_", " ");
            String firstChar = String.valueOf(formatted.charAt(0)).toUpperCase();
            return firstChar + formatted.substring(1);
        }
    }

}