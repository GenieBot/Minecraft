package io.sponges.bot.modules.minecraft.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;

public class AvatarCommand extends Command {

    private static final String REQUEST_URL = "https://mcapi.ca/avatar/2d/";

    public AvatarCommand() {
        super("gets an image of the head of a player in 2D", "mcavatar", "mchead");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: mcavatar [player]");
            return;
        }
        request.reply(REQUEST_URL + args[0]);
    }
}
