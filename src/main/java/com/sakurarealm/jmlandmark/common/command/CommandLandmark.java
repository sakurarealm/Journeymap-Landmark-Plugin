package com.sakurarealm.jmlandmark.common.command;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.server.ServerProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;
import java.util.List;

public class CommandLandmark extends CommandBase {
    private static final List<String> aliases = Arrays.asList("landmark", "lm");

    @Override
    public String getName() {
        return "journeymaplandmark";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "\n 旅行地图地标指令 /journeymaplandmark, /landmark 或 /lm\n"
                + "/lm reload 重新为所有玩家加载地标（OP Only）\n";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            if (args.length < 1) {
                throw new WrongUsageException(getUsage(sender));
            }

            boolean isOp = !(sender instanceof EntityPlayer) || JMLandmarkMod.getProxy().isPlayerOp((EntityPlayerMP) sender.getCommandSenderEntity());

            String operation = args[0].toLowerCase();
            if (operation.equals("reload")) {
                if (!isOp) {
                    throw new CommandException("§c执行此命令需要OP权限");
                }
                ((ServerProxy) JMLandmarkMod.getProxy()).reload();
                sender.sendMessage(new TextComponentString("§a重载成功"));
            } else {
                throw new WrongUsageException(getUsage(sender));
            }
        } catch (Exception e) {
            throw e;
        }

    }
}
