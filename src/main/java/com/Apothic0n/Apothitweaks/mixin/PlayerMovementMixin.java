package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;

@Mixin(Player.class)
public abstract class PlayerMovementMixin extends LivingEntity {
    @Shadow @Final private Abilities abilities;

    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract boolean isAffectedByFluids();

    @Shadow public abstract boolean isCreative();

    protected PlayerMovementMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Unique
    private int apothitweaks$ticksSinceOnSprintableBlock = 15;

    /**
     * @author Apothicon
     * @reason Removes the jumping speed boost and makes sprinting work only on some blocks.
     */
    @Overwrite
    protected float getBlockSpeedFactor() {
        float speed = !this.abilities.flying && !this.isFallFlying() ? super.getBlockSpeedFactor() : 1.0F;
        BlockState blockOn = this.getBlockStateOn();
        if ((blockOn.getTags().toList().contains(BlockTags.MINEABLE_WITH_AXE) || blockOn.getTags().toList().contains(BlockTags.MINEABLE_WITH_PICKAXE) || blockOn.is(Blocks.GRAVEL)  || blockOn.is(BlockTags.WOOL)  ||
                blockOn.is(BlockTags.WOOL_CARPETS) || blockOn.is(Blocks.DIRT_PATH)) && blockOn.isSolid() && blockOn.getBlock().getFriction() == 0.6F) {
            apothitweaks$ticksSinceOnSprintableBlock = 0;
        } else {
            apothitweaks$ticksSinceOnSprintableBlock = apothitweaks$ticksSinceOnSprintableBlock+1;
        }
        if (this.isSprinting() && !this.isCreative()) {
            if (apothitweaks$ticksSinceOnSprintableBlock >= 15 && blockOn.isAir() && !this.abilities.flying) {
                return speed - 0.15F;
            } else if (apothitweaks$ticksSinceOnSprintableBlock < 15 && blockOn.isSolid() && blockOn.getBlock().getFriction() == 0.6F && !this.isSwimming()) {
                return speed + 0.3F;
            } else if (this.isSwimming() && this.isAffectedByFluids()) {
                return speed - 0.07F;
            } else if (apothitweaks$ticksSinceOnSprintableBlock >= 15) {
                return speed - 0.3F;
            }
        }
        return speed;
    }
}
