package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow @Final private Abilities abilities;

    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract boolean isAffectedByFluids();

    @Shadow public abstract boolean isCreative();

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
        if (this.isSprinting() && !this.isCreative()) {
            if (blockOn.isAir() && !this.abilities.flying) {
                return speed - 0.15F;
            } else if ((blockOn.getTags().toList().contains(BlockTags.MINEABLE_WITH_AXE) || blockOn.getTags().toList().contains(BlockTags.MINEABLE_WITH_PICKAXE)) &&
                    blockOn.isSolid() && blockOn.getBlock().getFriction() == 0.6F && speed == 1.0F && !this.isSwimming()) {
                return speed + 0.3F;
            } else if (this.isSwimming() && this.isAffectedByFluids()) {
                return speed - 0.07F;
            } else {
                return speed - 0.3F;
            }
        }
        return speed;
    }

    /**
     * @author Apothicon
     * @reason Makes xp levels a flat amount of xp.
     */
    @Overwrite
    public int getXpNeededForNextLevel() {
        return 50;
    }

    /**
     * @author Apothicon
     * @reason Makes items not scatter randomly upon death.
     */
    @ModifyVariable(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private boolean injected(boolean p_36180_) {
        return false;
    }
}
