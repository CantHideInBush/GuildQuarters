package com.canthideinbush.guildquarters;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.commands.generators.StartCommand;
import com.canthideinbush.guildquarters.quarters.*;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerators;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.ConstantGeneratorBuilder;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematics;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructures;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.CHIBPlugin;
import com.canthideinbush.utils.storing.YAMLConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Function;

public final class GuildQ extends CHIBPlugin {

    static {
        ConfigurationSerialization.registerClass(GuildQuarter.class);
        ConfigurationSerialization.registerClass(QuarterTier.class);
        ConfigurationSerialization.registerClass(QuarterSchematics.class);
        ConfigurationSerialization.registerClass(QuarterRegion.class);
        ConfigurationSerialization.registerClass(QuarterSchematic.class);
        ConfigurationSerialization.registerClass(QuarterObjects.class);
        ConfigurationSerialization.registerClass(QuarterSchematics.class);
        ConfigurationSerialization.registerClass(QuarterStructures.class);
        ConfigurationSerialization.registerClass(ItemGenerators.class);

    }



    private static GuildQ instance;


    public static GuildQ getInstance() {
        return instance;
    }

    private YAMLConfig config;
    private YAMLConfig messageConfig;
    private YAMLConfig quartersStorage;

    private YAMLConfig itemsStorage;

    private QuartersManager quartersManager;
    private QuarterSchematics quarterSchematics;
    private QuarterStructures quarterStructures;

    private ItemGenerators itemGenerators;


    private static final HashMap<Class<?>, Function<Object, String>> serializers = new HashMap<>();
    private static final HashMap<Class<?>, Function<String, Object>> deserializers = new HashMap<>();

    static {

    }



    @Override
    public void onEnable() {
        instance = this;

        serializers.forEach(YAMLConfig::registerSerializer);
        deserializers.forEach(YAMLConfig::registerDeserializer);

        loadConfigurations();



        CHIBInit();

        GuildUtils.createGuildWorld();

        loadCommands();

        loadManagers();

        loadListeners();





    }

    @Override
    public void onDisable() {
        saveManagers();

        saveConfigurations();
    }


    private void loadManagers() {
        quarterSchematics = quartersStorage.contains("Schematics") ? (QuarterSchematics) quartersStorage.get("Schematics") : new QuarterSchematics();
        quarterStructures = quartersStorage.contains("Structures") ? (QuarterStructures) quartersStorage.get("Structures") : new QuarterStructures();
        itemGenerators = quartersStorage.contains("ItemGenerators") ? (ItemGenerators) quartersStorage.get("ItemGenerators") : new ItemGenerators();


        QuarterTiers.load();
        QuarterRegion.init();
        quartersManager = new QuartersManager();
        quartersManager.initialize();

    }

    private void saveManagers() {
        QuarterTiers.save();
        quartersManager.save();
        quarterSchematics.save();
        quarterStructures.save();
        itemGenerators.save();
    }



    private void loadConfigurations() {
        config = new YAMLConfig(this, "config", true);
        messageConfig = new YAMLConfig(this, "messages", true);
        quartersStorage = new YAMLConfig(this, "quarters", false);
        itemsStorage = new YAMLConfig(this, "items", false);
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new QuartersListener(), this);
    }

    private void saveConfigurations() {
        config.save();
        messageConfig.save();
        quartersStorage.save();
        itemsStorage.save();
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

        StartCommand.builders.put("constant", ConstantGeneratorBuilder.class);

    }

    public QuartersManager getQuartersManager() {
        return quartersManager;
    }

    public QuarterSchematics getQuarterSchematics() {
        return quarterSchematics;
    }

    public QuarterStructures getQuarterStructures() {
        return quarterStructures;
    }

    public ItemGenerators getItemGenerators() {
        return itemGenerators;
    }

    public YAMLConfig getQuartersStorage() {
        return quartersStorage;
    }

    public YAMLConfig getItemsStorage() {
        return itemsStorage;
    }
}
