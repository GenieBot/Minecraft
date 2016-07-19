package io.sponges.bot.modules.minecraft.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MCServerCommand extends Command {

    private static final String REQUEST_URL = "http://mcapi.ca/query/%s/info";

    public MCServerCommand() {
        super("shows minecraft server list ping info", "mcserver", "slp", "serverlistping", "mcserverstatus", "minecraftserverlistping");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: mcserver [ip]");
            return;
        }
        String ip = args[0];
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(String.format(REQUEST_URL, ip)).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            request.reply("Something went wrong :(");
            return;
        }
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 SpongyBot");
        connection.addRequestProperty("X-Request-From-SpongyBot", "client=" + request.getClient().getId() + "network="
                + request.getNetwork().getId() + "channel=" + request.getChannel().getId() + "user="
                + request.getUser().getId());
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
        JSONObject object = new JSONObject(builder.toString());
        if (!object.getBoolean("status")) {
            request.reply("Could not ping that server. Is it offline?");
            return;
        }
        String motd = object.getString("motd");
        JSONObject players = object.getJSONObject("players");
        int online = players.getInt("online");
        int max = players.getInt("max");
        request.reply("\"" + ip + "\" (" + online + " / " + max + ")\n" + motd);
    }
}