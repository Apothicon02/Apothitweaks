package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @Shadow private int foodLevel = 10;
    @Shadow private int lastFoodLevel = 10;
    @Shadow private float saturationLevel = 0.0F;
    @Shadow private float exhaustionLevel = 0.0F;

    /**
     * @author Apothicon
     * @reason Disables hunger & makes food heal you.
     */
    @Overwrite
    public void tick(Player player) {
        int additionalHP = (this.foodLevel - this.lastFoodLevel)/2;
        if (player.hasEffect(MobEffects.HUNGER) && additionalHP > 1) {
            additionalHP = 1;
        }
        if (additionalHP > 0 && player.isAlive()) {
            if (player.hasEffect(MobEffects.SATURATION)) {
                additionalHP = additionalHP*2;
            }
            player.setHealth(Math.min(additionalHP + player.getHealth(), player.getMaxHealth()));
        }
        this.foodLevel = 10;
        this.lastFoodLevel = 10;
        this.saturationLevel = 0.0F;
        this.exhaustionLevel = 0.0F;
    }
}
