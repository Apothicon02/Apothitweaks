package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Shadow public int lifespan;

    /**
     * @author Apothicon
     * @reason Makes items take quadruple the time to despawn.
     */
    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (lifespan == 6000) {
            lifespan = 24000;
        }
    }
}
