package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract void setNeverPickUp();

    @Shadow private int age;

    @Shadow public abstract ItemStack getItem();

    public ItemEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    /**
     * @author Apothicon
     * @reason Prevents /give command from spawning a fake item that lasts longer than 1 tick.
     */
    @Inject(method = "makeFakeItem", at = @At("TAIL"))
    public void makeFakeItem(CallbackInfo ci) {
        int newAge = getItem().getEntityLifespan(this.level());
        if (newAge == 6000) {
            newAge = 36000;
            this.age = newAge - 1;
        }
    }
}
