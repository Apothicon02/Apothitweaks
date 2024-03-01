package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public abstract class PlayerXPMixin extends LivingEntity {
    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract boolean isAffectedByFluids();

    protected PlayerXPMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    /**
     * @author Apothicon
     * @reason Makes xp levels a flat amount of xp.
     */
    @Overwrite
    public int getXpNeededForNextLevel() {
        return 50;
    }
}
