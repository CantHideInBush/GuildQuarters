package com.canthideinbush.guildquarters.commands.item;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ConfigGeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.ConfigGeneratorItemBuilder;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorItemBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.builder.*;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuildCommand extends BuilderCommand<GeneratorItem, GeneratorItemBuilder> {

    public ItemBuildCommand() {
        defaultInit();
        saveDefaultConfigMessages();
    }

    protected void defaultInit() {
        subCommands.add(new StartCommand(this) {
            @Override
            public Class<? extends InternalCommand> getParentCommandClass() {
                return ItemBuildCommand.class;
            }
        });
        subCommands.add(new WithCommand(this) {
            @Override
            public Class<? extends InternalCommand> getParentCommandClass() {
                return ItemBuildCommand.class;
            }

            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length == getArgIndex() + 1) {
                    args = Arrays.copyOf(args, args.length + 1);
                    args[getArgIndex() + 1] = " ";
                }

                return super.execute(sender, args);
            }

            /*
                        @Override
            public List<String> complete(String[] args, CommandSender sender) {
                if (args.length > getArgIndex() + 1) args = Arrays.copyOf(args, args.length + 1);
                args[args.length - 1] = " ";
                return super.complete(args, sender);
            }
             */

        });
        subCommands.add(new CompleteCommand(this) {
            @Override
            public Class<? extends InternalCommand> getParentCommandClass() {
                return ItemBuildCommand.class;
            }
        });
        subCommands.add(new EditCommand<>(this) {
            @Override
            public Class<? extends InternalCommand> getParentCommandClass() {
                return ItemBuildCommand.class;
            }
        });
    }

    @Override
    public Class<? extends GeneratorItemBuilder> getBuilderFor(GeneratorItem item) {
        if (item instanceof ConfigGeneratorItem) return ConfigGeneratorItemBuilder.class;
        return null;
    }

    @Override
    public GeneratorItem findById(String s) {
        return GeneratorItem.get(s);
    }

    @Override
    public List<String> getIdCompletion() {
        return GeneratorItem.getIds();
    }

    @DefaultConfigMessage(forN = "invalid-item")
    private static final String INVALID_ITEM = "Trzymasz niepoprawny typ przedmiotu!";

    public static HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();
    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    protected void completeAction(GeneratorItem item) {
        GeneratorItem.register(item);
    }

    @Override
    public void setBuilder(CommandSender player, ObjectBuilder<?> builder) {
        super.setBuilder(player, builder);
        ((GeneratorItemBuilder) builder).setSender(player);
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return ItemParentCommand.class;
    }
}
