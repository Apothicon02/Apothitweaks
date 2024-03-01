package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityFootstepsMixin {

    @Shadow public abstract SoundSource getSoundSource();

    @Shadow public abstract boolean isSilent();

    @Shadow public abstract Level level();

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    /**
     * @author Apothicon
     * @reason Makes hostile mobs have louder footsteps.
     */
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    public void playSound(SoundEvent event, float volume, float pitch, CallbackInfo ci) {
        if (!this.isSilent() && event.getLocation().toString().contains("step") && this.getSoundSource().equals(SoundSource.HOSTILE)) {
            this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), event, this.getSoundSource(), volume*3, pitch);
        }
    }
}
