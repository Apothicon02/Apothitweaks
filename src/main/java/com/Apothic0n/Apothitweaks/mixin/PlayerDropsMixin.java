package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerDropsMixin extends LivingEntity {
    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract boolean isAffectedByFluids();

    protected PlayerDropsMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }


    /**
     * @author Apothicon
     * @reason Makes items not scatter randomly upon death.
     */
    @ModifyVariable(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private boolean drop(boolean p_36180_) {
        return false;
    }
}
