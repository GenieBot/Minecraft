package io.sponges.bot.modules.minecraft.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;

public class SkinCommand extends Command {

    private static final String REQUEST_URL = "https://mcapi.ca/skin/2d/";

    public SkinCommand() {
        super("gets an image of the character model of a player in 2D", "mcskin");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: mcskin [player]");
            return;
        }
        request.reply(REQUEST_URL + args[0]);
    }
}
