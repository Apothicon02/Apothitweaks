package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {


    protected LivingEntityMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Shadow public abstract boolean canBeAffected(MobEffectInstance p_21197_);

    @Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;

    @Shadow protected abstract void onEffectAdded(MobEffectInstance p_147190_, @Nullable Entity p_147191_);

    @Shadow protected abstract void onEffectUpdated(MobEffectInstance p_147192_, boolean p_147193_, @Nullable Entity p_147194_);

    /**
     * @author Apothicon
     * @reason Makes poison last 33% as long.
     */
    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    public void addEffect(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.canBeAffected(effectInstance) && effectInstance.getEffect().equals(MobEffects.POISON)) {
            MobEffectInstance mobeffectinstance = this.activeEffects.get(effectInstance.getEffect());
            MobEffectInstance newMobEffect = new MobEffectInstance(MobEffects.POISON, effectInstance.getDuration()/3, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon(), null, effectInstance.getFactorData());
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.MobEffectEvent.Added((LivingEntity) (Object) (this), mobeffectinstance, effectInstance, entity));
            if (mobeffectinstance == null) {
                this.activeEffects.put(newMobEffect.getEffect(), newMobEffect);
                this.onEffectAdded(newMobEffect, entity);
                cir.setReturnValue(true);
            } else if (mobeffectinstance.update(newMobEffect)) {
                this.onEffectUpdated(mobeffectinstance, true, entity);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }
}
