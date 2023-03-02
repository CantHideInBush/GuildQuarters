package com.canthideinbush.guildquarters;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuartersListener;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.CHIBPlugin;
import com.canthideinbush.utils.storing.YAMLConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

public final class GuildQ extends CHIBPlugin {

    static {
        ConfigurationSerialization.registerClass(GuildQuarter.class);

    }



    private static GuildQ instance;
    public static GuildQ getInstance() {
        return instance;
    }

    private YAMLConfig config;
    private YAMLConfig messageConfig;
    private YAMLConfig quartersStorage;

    private QuartersManager quartersManager;


    @Override
    public void onEnable() {
        instance = this;


        // Plugin startup logic

        loadConfigurations();


        CHIBInit();

        loadCommands();

        loadManagers();

        loadListeners();


        GuildUtils.createGuildWorld();


    }

    @Override
    public void onDisable() {
        saveManagers();

        saveConfigurations();
    }


    private void loadManagers() {
        quartersManager = new QuartersManager();
        quartersManager.load();
    }

    private void saveManagers() {
        quartersManager.save();
    }



    private void loadConfigurations() {
        config = new YAMLConfig(this, "config", true);
        messageConfig = new YAMLConfig(this, "messages", true);
        quartersStorage = new YAMLConfig(this, "quarters", false);
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new QuartersListener(), this);
    }

    private void saveConfigurations() {
        config.save();
        messageConfig.save();
        quartersStorage.save();
    }

    @Override
    public String getPrefix() {
        return ChatColor.RED + "[" + ChatColor.GREEN + "GuildQuarters" + ChatColor.RED + "]";
    }

    @Override
    public YAMLConfig getMessageConfig() {
        return messageConfig;
    }

    @NotNull
    @Override
    public YAMLConfig getConfig() {
        return config;
    }

    private void loadCommands() {
        new MainCommand(this);
    }

    public QuartersManager getQuartersManager() {
        return quartersManager;
    }

    public YAMLConfig getQuartersStorage() {
        return quartersStorage;
    }
}
