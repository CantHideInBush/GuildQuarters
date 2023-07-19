package com.canthideinbush.guildquarters.commands.spawner;

import com.canthideinbush.guildquarters.quarters.spawners.MMSpawner;
import com.canthideinbush.guildquarters.quarters.spawners.MMSpawnerBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.builder.BuilderCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SpawnerBuildCommand extends BuilderCommand<MMSpawner, MMSpawnerBuilder> {


    public SpawnerBuildCommand() {
        defaultConstructor();
    }

    public static final HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();

    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    public void setBuilder(CommandSender player, ObjectBuilder<?> builder) {
        super.setBuilder(player, builder);
        ((MMSpawnerBuilder) builder).setSender((Player) player);
    }

    @Override
    public Class<? extends MMSpawnerBuilder> getBuilderFor(MMSpawner spawner) {
        if (spawner instanceof MMSpawner) return MMSpawnerBuilder.class;
        return null;
    }

    @Override
    public MMSpawner findById(String s) {
        return MMSpawner.findByName(s);
    }

    @Override
    public List<String> getIdCompletion() {
        return MMSpawner.getIds();
    }

    @Override
    protected void completeAction(MMSpawner spawner) {
        MMSpawner.register(spawner);
    }

    @Override
    public String getName() {
        return "build";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return SpawnerParentCommand.class;
    }

}
