package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Boat.class)
public abstract class BoatMixin extends Entity {
    @Shadow private boolean inputLeft;

    @Shadow private float deltaRotation;

    @Shadow private boolean inputRight;

    @Shadow private boolean inputUp;

    @Shadow private boolean inputDown;

    @Shadow public abstract void setPaddleState(boolean p_38340_, boolean p_38341_);

    public BoatMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    /**
     * @author Apothicon
     * @reason Makes boats 1.5x faster
     */
    @Overwrite
    private void controlBoat() {
        if (this.isVehicle()) {
            float f = 0.0F;
            if (this.inputLeft) {
                --this.deltaRotation;
            }

            if (this.inputRight) {
                ++this.deltaRotation;
            }

            if (this.level().getBlockState(this.blockPosition()).is(Blocks.WATER)) {
                if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                    f += 0.0075F;
                }

                this.setYRot(this.getYRot() + this.deltaRotation);
                if (this.inputUp) {
                    f += 0.06F;
                }

                if (this.inputDown) {
                    f -= 0.0075F;
                }
            } else {
                if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                    f += 0.00125F;
                }

                this.setYRot(this.getYRot() + this.deltaRotation);
                if (this.inputUp) {
                    f += 0.01F;
                }

                if (this.inputDown) {
                    f -= 0.01F;
                }
            }
            this.setDeltaMovement(this.getDeltaMovement().add((double)(Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * f), 0.0D, (double)(Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * f)));
            this.setPaddleState(this.inputRight && !this.inputLeft || this.inputUp, this.inputLeft && !this.inputRight || this.inputUp);
        }
    }
}
