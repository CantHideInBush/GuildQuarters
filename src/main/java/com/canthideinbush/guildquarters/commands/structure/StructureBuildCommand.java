package com.canthideinbush.guildquarters.commands.structure;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.guildquarters.quarters.structures.StructureBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.builder.BuilderCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StructureBuildCommand extends BuilderCommand<QuarterStructure, StructureBuilder> {

    public StructureBuildCommand() {
        defaultConstructor();
    }

    public static HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();

    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    public Class<? extends StructureBuilder> getBuilderFor(QuarterStructure structure) {
        if (structure != null) return StructureBuilder.class;
        return null;
    }

    @Override
    public QuarterStructure findById(String s) {
        return GuildQ.getInstance().getQuarterStructures().get(s);
    }

    @Override
    public List<String> getIdCompletion() {
        return GuildQ.getInstance().getQuarterStructures().getIds();
    }

    @Override
    protected void completeAction(CommandSender sender, QuarterStructure structure) {

        if (isEditing(sender)) {
            HashMap<GuildQuarter, StructureStorage> toUpdate = new HashMap<>();

            for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
                QuarterStructure oldStructure;
                if ((oldStructure = quarter.getQuarterObjects().getStructure(structure.getId())) != null) {
                    toUpdate.put(quarter, oldStructure.getStorage());
                }
            }
            GuildQ.getInstance().getQuarterStructures().removeStructure(structure.getId());

            toUpdate.forEach((q, s) -> {
                q.getQuarterObjects().placeStructure(structure.clone());
                structure.setStorage(s);
            });
        }

        GuildQ.getInstance().getQuarterStructures().addStructure(structure);
    }


    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return StructureParentCommand.class;
    }

    @Override
    public void setBuilder(CommandSender player, ObjectBuilder<?> builder) {
        super.setBuilder(player, builder);
        ((StructureBuilder) builder).setSender((Player) player);
    }

    @Override
    protected List<Class<?>> getAdditionalMessageClasses() {
        return Collections.singletonList(StructureBuilder.class);
    }
}
