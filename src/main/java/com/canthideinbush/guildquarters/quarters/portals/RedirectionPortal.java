package com.canthideinbush.guildquarters.quarters.portals;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Portals.CMIPortal;
import com.Zrips.CMI.Modules.Portals.CuboidArea;
import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterObject;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.util.Vector;

import java.util.logging.Level;

public class RedirectionPortal implements ABSave, QuarterObject {

    /**
     * !!! Cloned object !!!
     */

    private CMIPortal target;

    @YAMLElement
    private String targetName;

    @YAMLElement
    private String name;

    @YAMLElement
    private Vector offset;

    @YAMLElement
    private Vector offset1;

    @YAMLElement
    private boolean isDefault = false;

    private CMIPortal portal;
    private GuildQuarter quarter;

    public RedirectionPortal(String name, String target, Vector offset, Vector offset1, boolean isDefault) {
        this.name = name;
        this.targetName = target;
        this.offset = offset;
        this.offset1 = offset1;
        this.isDefault = isDefault;
    }

    public CMIPortal getOrCreatePortal() {
        CMIPortal portal;
        if ((portal = CMI.getInstance().getPortalManager().getByName(getCMIPortalName())) == null) {
            portal = new CMIPortal();
            portal.setName(getCMIPortalName());
        }
        return portal;
    }


    //report=redirection portal
    public String getCMIPortalName() {
        return "guildq_report_" + quarter.getShortId() + "_" + name;
    }

    public void initialize(GuildQuarter quarter) {
        this.quarter = quarter;
        this.portal = getOrCreatePortal();
        this.target = CMI.getInstance().getPortalManager().getByName(targetName);
        if (target == null) {
            GuildQ.getInstance().getLogger().log(Level.WARNING, "Portal initialization failed: " + name + ". Target portal '" + targetName + "' is not existing");
            return;
        }
        portal.setPerformCommandsWithoutTp(target.getPerformCommandsWithoutTp());
        portal.setEnabled(target.isEnabled());
        portal.setShowParticles(target.isShowParticles());
        portal.setParticleAmount(target.getParticleAmount());
        portal.setRequiresPermission(target.isRequiresPermission());
        portal.setKickBack(target.isKickBack());
        portal.setInformOnMissingPerm(target.isInformOnMissingPerm());
        portal.setPercentToHide(target.getPercentToHide());
        portal.setActivationRange(target.getActivationRange());
        portal.setCMIEffect(target.getCMIEffect());
        portal.setCommands(target.getCommands());
        portal.setParticlesByPermission(target.isParticlesByPermission());
        portal.setParticleForPlayers(target.getParticleForPlayers());
        portal.setTpLoc(target.getTpLoc());
        portal.setArea(new CuboidArea(quarter.getInitialLocation().add(offset), quarter.getInitialLocation().add(offset1)), true);
        if (CMI.getInstance().getPortalManager().getByName(getCMIPortalName()) == null) {
            CMI.getInstance().getPortalManager().addPortal(portal);
        }
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void place() {

    }

    public void remove() {
        CMI.getInstance().getPortalManager().removePortal(portal);
    }


    public String getName() {
        return name;
    }

    public RedirectionPortal clone() {
        return new RedirectionPortal(name, targetName, offset, offset1, isDefault);
    }

    public String getTargetName() {
        return targetName;
    }

    public Vector getOffset() {
        return offset;
    }
    public Vector getOffset1() {
        return offset1;
    }
}
