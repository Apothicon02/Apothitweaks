package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity implements net.minecraftforge.common.extensions.IForgeAbstractMinecart {
    public AbstractMinecartMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "getMaxSpeedWithRail", at = @At("HEAD"), cancellable = true, remap = false)
    public void getMaxSpeedWithRail(CallbackInfoReturnable<Double> cir) {
        Level level = this.level();
        BlockPos origin = this.blockPosition();
        boolean shouldBoost = true;
        for (int x = origin.getX()-2; x <= origin.getX()+2; x++) {
            for (int z = origin.getZ()-2; z <= origin.getZ()+2; z++) {
                BlockPos blockPos = new BlockPos(x, origin.getY(), z);
                if (level.hasChunkAt(blockPos)) {
                    BlockState blockState = level.getBlockState(blockPos);
                    RailShape shape = null;
                    if (blockState.hasProperty(BlockStateProperties.RAIL_SHAPE)) {
                        shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE);
                    } else if (blockState.hasProperty(BlockStateProperties.RAIL_SHAPE_STRAIGHT)) {
                        shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
                    }
                    if (shape != null && (shape.isAscending() || shape.equals(RailShape.NORTH_EAST) || shape.equals(RailShape.NORTH_WEST) || shape.equals(RailShape.SOUTH_EAST) || shape.equals(RailShape.SOUTH_WEST))) {
                        shouldBoost = false;
                        x = x+100;
                        z = z+100;
                    }
                }
            }
        }
        if (shouldBoost == false) {
            cir.setReturnValue(0.3);
        } else {
            cir.setReturnValue(1.6);
        }
    }
}
