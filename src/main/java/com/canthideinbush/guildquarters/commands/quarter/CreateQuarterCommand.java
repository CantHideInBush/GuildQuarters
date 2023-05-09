package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CreateQuarterCommand extends InternalCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public String getPermission() {
        return "create";
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        if (args.length == getArgIndex() - 1 &&
                (sender instanceof ConsoleCommandSender || sender.hasPermission(ADMIN_PERMISSION))) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Pomyslnie utworzono siedzibe!";

    @Override
    public boolean execute(Player sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        OfflinePlayer target;

        if (parser.hasNext() && sender.hasPermission(ADMIN_PERMISSION)) {
            target = Bukkit.getOfflinePlayer(parser.next());
        }
        else target = sender;

        Guild guild = Guilds.getApi().getGuild(target);
        if (guild == null) {
            sendConfigErrorMessage(sender, "guildq-guilds-not_member");
            return false;
        }
        else if (!guild.getGuildMaster().getAsOfflinePlayer().equals(target)) {
            sendConfigErrorMessage(sender, "guildq-guilds-not_master");
            return false;
        }

        if (!GuildQ.getInstance().getQuartersManager().createGuildQuarter(guild)) {
            sendConfigErrorMessage(sender, "guildq-guilds-create-exists");
            return false;
        }

        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    public final String ADMIN_PERMISSION = getAbsolutePermission() + ".admin";

    @Override
    public boolean execute(ConsoleCommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(parser.next());
        Guild guild = Guilds.getApi().getGuild(player);
        if (guild == null) {
            sendConfigErrorMessage(sender, "guildq-guilds-not_member");
            return false;
        }
        else if (!guild.getGuildMaster().getAsOfflinePlayer().equals(player)) {
            sendConfigErrorMessage(sender, "guildq-guilds-not_master");
            return false;
        }

        if (!GuildQ.getInstance().getQuartersManager().createGuildQuarter(guild)) {
            sendConfigErrorMessage(sender, "guildq-guilds-create-exists");
            return false;
        }


        sendConfigSuccessMessage(sender, getMessagePath("success"));



        return true;
    }
}
