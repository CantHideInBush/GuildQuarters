package com.canthideinbush.guildquarters;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.commands.generators.GeneratorBuildCommand;
import com.canthideinbush.guildquarters.commands.item.ItemBuildCommand;
import com.canthideinbush.guildquarters.commands.spawner.SpawnerBuildCommand;
import com.canthideinbush.guildquarters.commands.structure.StructureBuildCommand;
import com.canthideinbush.guildquarters.http.GQHttpServer;
import com.canthideinbush.guildquarters.quarters.*;
import com.canthideinbush.guildquarters.quarters.itemgenerators.*;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.ConfigGeneratorItemBuilder;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.ConstantGeneratorBuilder;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.MMOItemBuilder;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.RandomItemGeneratorBuilder;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematics;
import com.canthideinbush.guildquarters.quarters.spawners.MMSpawner;
import com.canthideinbush.guildquarters.quarters.spawners.MMSpawnerBuilder;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructures;
import com.canthideinbush.guildquarters.quarters.structures.StructureBuilder;
import com.canthideinbush.guildquarters.quarters.updating.QuartersUpdateQueue;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.CHIBPlugin;
import com.canthideinbush.utils.storing.YAMLConfig;
import net.citizensnpcs.Citizens;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanners;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

public final class GuildQ extends CHIBPlugin implements Listener {

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
        ConfigurationSerialization.registerClass(ConfigGeneratorItem.class);
        ConfigurationSerialization.registerClass(MMOGeneratorItem.class);
        ConfigurationSerialization.registerClass(ConstantItemGenerator.class);
        ConfigurationSerialization.registerClass(StructureStorageImpl.class);
        ConfigurationSerialization.registerClass(QuarterStructure.class);
        ConfigurationSerialization.registerClass(MMSpawner.class);
        ConfigurationSerialization.registerClass(RandomItemGenerator.class);

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

    private GQHttpServer httpServer;

    private QuartersUpdateQueue quartersUpdateQueue;


    private static final HashMap<Class<?>, Function<Object, String>> serializers = new HashMap<>();
    private static final HashMap<Class<?>, Function<String, Object>> deserializers = new HashMap<>();



    public static String getMessage(String path) {
        return instance.utilsProvider.getChatUtils().getMessage(path);
    }

    private boolean enabled = false;
    @Override
    public void onEnable() {
        instance = this;
        loadConfigurations();
        Bukkit.getPluginManager().registerEvents(this, this);
        GuildUtils.createGuildWorld();
    }


    private void enablePlugin() {
        if (enabled) return;
        enabled = true;

        serializers.forEach(YAMLConfig::registerSerializer);
        deserializers.forEach(YAMLConfig::registerDeserializer);



        CHIBInit();



        loadCommands();

        //requires post-world load
        quartersStorage = new YAMLConfig(this, "quarters", false);

        quartersUpdateQueue = new QuartersUpdateQueue();

        loadManagers();

        loadListeners();

        hookCitizens();

        try {
            initHttpServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initHttpServer() throws Exception {
        try (
               JarFile jarFile = new JarFile(getFile())
        ) {
            jarFile.stream().filter(e -> e.getName().matches("^https.*"))
                    .forEach(e -> {
                        String fileName = e.getName();
                        if (e.isDirectory()) {
                            fileName += File.separator;
                            new File(getDataFolder(), fileName).mkdir();
                            return;
                        }
                        File copyFile = new File(getDataFolder(), fileName);
                        try {
                            copyFile.createNewFile();
                            if (!e.isDirectory()) IOUtils.copy(jarFile.getInputStream(e), new FileOutputStream(copyFile));

                        } catch (
                                IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }


        try {
            saveResource("https/key.jks", false);
            httpServer = new GQHttpServer(getSLF4JLogger());
        } catch (
                UnrecoverableKeyException |
                NoSuchAlgorithmException |
                KeyStoreException |
                IOException |
                KeyManagementException |
                URISyntaxException |
                CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    private void hookCitizens() {
        if (citizens != null) {
            if (getQuartersManager() != null) getQuartersManager().getObjects().forEach(GuildQuarter::initializeNPC);
            return;
        }
        citizens = (Citizens) Bukkit.getPluginManager().getPlugin("Citizens");
        if (getQuartersManager() != null) getQuartersManager().getObjects().forEach(GuildQuarter::initializeNPC);
        getLogger().log(Level.INFO, "Hooked into Citizens!");
    }

    public static Citizens citizens;




    @Override
    public void onDisable() {
        saveManagers();

        saveConfigurations();

        enabled = false;

        super.onDisable();
    }


    private void loadManagers() {


        quarterSchematics = quartersStorage.contains("Schematics") ? (QuarterSchematics) quartersStorage.get("Schematics") : new QuarterSchematics();
        quarterStructures = quartersStorage.contains("Structures") ? (QuarterStructures) quartersStorage.get("Structures") : new QuarterStructures();
        itemGenerators = quartersStorage.contains("ItemGenerators") ? (ItemGenerators) quartersStorage.get("ItemGenerators") : new ItemGenerators();


        QuarterTiers.load();
        QuarterRegion.init();



        //QuartersManager is loaded in world load event below

    }


    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) throws IOException {
        if (event.getWorld().equals(GuildUtils.getGuildWorld())) {
            enablePlugin();
            loadQuarters();
        }
    }

    private void loadQuarters() {
        hookCitizens();

        MMSpawner.load();
        quartersManager = new QuartersManager();


    }




    /**
     * Reloads configurations, skips storages due to possible errors
     */
    public void reload() {
        config = new YAMLConfig(this, "config", true);
        messageConfig = new YAMLConfig(this, "messages", true);
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


        itemsStorage = new YAMLConfig(this, "items", false);
        GeneratorItem.load();


        //QuartersStorage loaded also in event!!!


    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new QuartersListener(), this);
    }

    private void saveConfigurations() {
        config.save();
        messageConfig.save();
        MMSpawner.save();
        quartersStorage.save();

        GeneratorItem.save();

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
        new MainCommand(this).saveDefaultConfigMessages();

        GeneratorBuildCommand.builders.put("constant", ConstantGeneratorBuilder.class);
        GeneratorBuildCommand.builders.put("random", RandomItemGeneratorBuilder.class);

        ItemBuildCommand.builders.put("config", ConfigGeneratorItemBuilder.class);
        ItemBuildCommand.builders.put("mmo", MMOItemBuilder.class);



        StructureBuildCommand.builders.put("simple", StructureBuilder.class);


        SpawnerBuildCommand.builders.put("default", MMSpawnerBuilder.class);


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

    public void setQuartersManager(QuartersManager quartersManager) {
        this.quartersManager = quartersManager;
    }

    public QuartersUpdateQueue getQuartersUpdateQueue() {
        return quartersUpdateQueue;
    }
}
