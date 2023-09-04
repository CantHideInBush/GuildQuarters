package com.canthideinbush.guildquarters.commands.redirectionportal;

import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortal;
import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortalBuilder;
import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortals;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.builder.BuilderCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RedirectionPortalBuildCommand extends BuilderCommand<RedirectionPortal, RedirectionPortalBuilder> {

    public static HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();

    public RedirectionPortalBuildCommand() {
        defaultConstructor();
    }

    @Override
    protected void completeAction(RedirectionPortal object) {
        RedirectionPortals.register(object);
    }

    @Override
    public void setBuilder(CommandSender player, ObjectBuilder<?> builder) {
        ((RedirectionPortalBuilder) builder).setSender((Player) player);
        super.setBuilder(player, builder);
    }

    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    public Class<? extends RedirectionPortalBuilder> getBuilderFor(RedirectionPortal portal) {
        return RedirectionPortalBuilder.class;
    }

    @Override
    public RedirectionPortal findById(String s) {
        return RedirectionPortals.getByName(s);
    }


    @Override
    public List<String> getIdCompletion() {
        return RedirectionPortals.getIds();
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return RedirectionPortalParentCommand.class;
    }

    @Override
    protected List<Class<?>> getAdditionalMessageClasses() {
        return Collections.singletonList(RedirectionPortalBuilder.class);
    }
}
