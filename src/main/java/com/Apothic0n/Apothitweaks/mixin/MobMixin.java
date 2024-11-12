package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "isSunBurnTick", at = @At("HEAD"), cancellable = true)
    protected void isSunBurnTick(CallbackInfoReturnable<Boolean> ci) {
        Level level = this.level();
        if (!level.isClientSide) {
            BlockPos blockpos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
            float f = level.getBrightness(LightLayer.BLOCK, blockpos) + (level.isDay() ? (this.level().canSeeSky(blockpos) ? level.getBrightness(LightLayer.SKY, blockpos) : 0) : 0);
            boolean flag = this.isInWaterRainOrBubble() || this.isInPowderSnow || this.wasInPowderSnow;
            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag) {
                ci.setReturnValue(true);
            }
        }
    }
}
