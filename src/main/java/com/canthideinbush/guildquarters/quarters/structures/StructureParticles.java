package com.canthideinbush.guildquarters.quarters.structures;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.VoxelShape;

public class StructureParticles {


    public static void highlightBlock(Block block) {
        Particle particle = Particle.REDSTONE;
        Particle.DustOptions options = new Particle.DustOptions(Color.GREEN, 0.5f);
        VoxelShape shape = block.getCollisionShape();
        Location blockLocation = block.getLocation();
        World world = blockLocation.getWorld();
        for (BoundingBox box : shape.getBoundingBoxes()) {
            for (double x = box.getMinX(); x < box.getMaxX(); x+= 0.1) {
                world.spawnParticle(particle, blockLocation.clone().add(x, box.getMinY(), box.getMinZ()), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(x, box.getMaxY(), box.getMaxZ()), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(x, box.getMaxY(), box.getMinZ()), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(x, box.getMaxY(), box.getMaxZ()), 1, options);
            }
            for (double z = box.getMinZ(); z < box.getMaxZ(); z+= 0.1) {
                world.spawnParticle(particle, blockLocation.clone().add(box.getMinX(), box.getMinY(), z), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(box.getMaxX(), box.getMinY(), z), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(box.getMinX(), box.getMaxY(), z), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(box.getMaxX(), box.getMaxY(), z), 1, options);
            }
            for (double y = box.getMinY(); y < box.getMaxY(); y+= 0.1) {
                world.spawnParticle(particle, blockLocation.clone().add(box.getMinX(), y, box.getMinZ()), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(box.getMaxX(), y, box.getMinZ()), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(box.getMinX(), y, box.getMaxX()), 1, options);
                world.spawnParticle(particle, blockLocation.clone().add(box.getMaxX(), y, box.getMaxZ()), 1, options);
            }

        }
    }



}
