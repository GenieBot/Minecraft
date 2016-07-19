package io.sponges.bot.modules.minecraft;

import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.modules.minecraft.cmd.*;

public class Minecraft extends Module {

    public Minecraft() {
        super("Minecraft", "1.05");
    }

    @Override
    public void onEnable() {
        CommandManager commandManager = getCommandManager();
        commandManager.registerCommand(this, new MCStatusCommand());
        commandManager.registerCommand(this, new AvatarCommand());
        commandManager.registerCommand(this, new HasPaidCommand());
        commandManager.registerCommand(this, new MCUUIDCommand());
        commandManager.registerCommand(this, new SkinCommand());
        commandManager.registerCommand(this, new SkinFileCommand());
        commandManager.registerCommand(this, new MCServerCommand());
    }

    @Override
    public void onDisable() {

    }
}
