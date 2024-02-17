package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster {
    @Shadow public abstract boolean isPowered();

    @Shadow private int explosionRadius;

    @Shadow protected abstract void spawnLingeringCloud();

    protected CreeperMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    /**
     * @author Apothicon
     * @reason Makes creepers not destroy blocks when they explode.
     */
    @Overwrite
    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, Level.ExplosionInteraction.NONE);
            for (int x = this.getBlockX() - 2; x <= this.getBlockX() + 2; x++) {
                for (int z = this.getBlockZ() - 2; z <= this.getBlockZ() + 2; z++) {
                    BlockPos pos = new BlockPos(x, (int) (this.getBlockY() + ((Math.random()*4)-2)), z);
                    BlockState fireSpot = this.level().getBlockState(pos);
                    if (fireSpot.canBeReplaced() && !fireSpot.liquid() && this.level().getBlockState(pos.below()).isSolid()) {
                        this.level().setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL);
                    }
                }
            }
            this.discard();
            this.spawnLingeringCloud();
        }

    }
}
