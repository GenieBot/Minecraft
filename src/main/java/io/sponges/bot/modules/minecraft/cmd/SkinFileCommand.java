package io.sponges.bot.modules.minecraft.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;

public class SkinFileCommand extends Command {

    private static final String REQUEST_URL = "https://mcapi.ca/skin/file/";

    public SkinFileCommand() {
        super("gets the skin file of a minecraft character", "skinfile", "mcskinfile", "fullskin");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: skinfile [player]");
            return;
        }
        request.reply(REQUEST_URL + args[0]);
    }
}
