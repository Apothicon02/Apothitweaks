package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Vex.class)
public class VexMixin {
    /**
     * @author Apothicon
     * @reason Reduces vex damage and health to 25%.
     */
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 3.5D).add(Attributes.ATTACK_DAMAGE, 1.0D);
    }
}
