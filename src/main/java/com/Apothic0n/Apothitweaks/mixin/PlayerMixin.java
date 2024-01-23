package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow @Final private Abilities abilities;

    @Shadow public abstract boolean isSwimming();

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    /**
     * @author Apothicon
     * @reason Removes the jumping speed boost and makes sprinting work only on some blocks.
     */
    @Overwrite
    protected float getBlockSpeedFactor() {
        float speed = !this.abilities.flying && !this.isFallFlying() ? super.getBlockSpeedFactor() : 1.0F;
        BlockState blockOn = this.getBlockStateOn();
        if (blockOn.isAir() && !this.abilities.flying && this.isSprinting()) {
            return speed-0.2F;
        } else if (!Set.of(Blocks.SNOW_BLOCK, Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.PODZOL, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT, Blocks.MUD, Blocks.SAND, Blocks.RED_SAND, Blocks.SOUL_SAND, Blocks.NETHERRACK).contains(blockOn.getBlock()) &&
                blockOn.isSolid() && blockOn.getBlock().getFriction() == 0.6F && speed == 1.0F && !this.isSwimming() && this.isSprinting()) {
            return speed+0.5F;
        } else if (this.isSprinting()) {
            return speed-0.2F;
        }
        return speed;
    }
}
