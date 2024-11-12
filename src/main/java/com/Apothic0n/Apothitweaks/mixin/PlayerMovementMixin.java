package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Player.class)
public abstract class PlayerMovementMixin extends LivingEntity {
    @Shadow @Final private Abilities abilities;

    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract boolean isAffectedByFluids();

    @Shadow public abstract boolean isCreative();

    protected PlayerMovementMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }
    /**
     * @author Apothicon
     * @reason Removes the jumping speed boost and makes sprinting work only when near a light source or when wearing frostwalker boots on ice.
     */
    @Overwrite
    protected float getBlockSpeedFactor() {
        float speed = !this.abilities.flying && !this.isFallFlying() ? super.getBlockSpeedFactor() : 1.0F;
        BlockState blockOn = this.getBlockStateOn();
        AtomicInteger brightness = new AtomicInteger(this.level().getBrightness(LightLayer.BLOCK, this.blockPosition()));
        if (blockOn.is(BlockTags.ICE)) {
            this.getArmorSlots().forEach((ItemStack itemStack) -> {
                if (itemStack.getEnchantmentLevel(Enchantments.FROST_WALKER) > 0) {
                    brightness.set(15);
                }
            });
        }
        if (this.isSprinting() && !this.isCreative()) {
            if (brightness.get() <= 0 && blockOn.isAir() && !this.abilities.flying) {
                return speed - 0.15F;
            } else if (brightness.get() > 15 && blockOn.isSolid() && blockOn.getBlock().getFriction() == 0.6F && !this.isSwimming()) {
                return speed + (0.02F* brightness.get());
            } else if (this.isSwimming() && this.isAffectedByFluids()) {
                return speed - 0.07F;
            } else if (brightness.get() <= 0) {
                return speed - 0.3F;
            }
        }
        return speed;
    }
}
