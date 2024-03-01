package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Entity.class)
public abstract class EntityMinecartRotationMixin {
    @Unique
    private float ridingEntityYawDelta;
    @Shadow
    public abstract Entity getVehicle();

    @Shadow public abstract void setYRot(float p_146923_);

    @Shadow public abstract float getYRot();

    @Inject(method = "rideTick", at = @At("HEAD"))
    private void rideTick(CallbackInfo ci) {
        if (this.getVehicle().getControllingPassenger() != (Object) this) {
            this.ridingEntityYawDelta = this.ridingEntityYawDelta + this.getVehicle().getYRot() - this.getVehicle().yRotO;

            while (this.ridingEntityYawDelta >= 180.0) {
                this.ridingEntityYawDelta -= 360.0F;
            }

            while (this.ridingEntityYawDelta < -180.0) {
                this.ridingEntityYawDelta += 360.0F;
            }

            var ridingEntityYawDeltaSmooth = this.ridingEntityYawDelta;

            var maxTurn = 360F;
            if (ridingEntityYawDeltaSmooth > maxTurn) {
                ridingEntityYawDeltaSmooth = maxTurn;
            }

            if (ridingEntityYawDeltaSmooth < -maxTurn) {
                ridingEntityYawDeltaSmooth = -maxTurn;
            }

            this.ridingEntityYawDelta -= ridingEntityYawDeltaSmooth;
            this.setYRot(this.getYRot() + ridingEntityYawDeltaSmooth);
        }
    }

}