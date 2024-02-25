package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin{

    @Shadow
    protected static BlockHitResult getPlayerPOVHitResult(Level p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        float f = p_41437_.getXRot();
        float f1 = p_41437_.getYRot();
        Vec3 vec3 = p_41437_.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = p_41437_.getBlockReach();
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return p_41436_.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, p_41438_, p_41437_));
    }

    @Inject(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;", at = @At("TAIL"), cancellable = true)
    public void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (itemStack.is(Items.STICK)) {
            if (blockhitresult.getType().equals(HitResult.Type.BLOCK)) {
                if (level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.TORCHFLOWER) || level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.CAMPFIRE) || level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.FIRE) ||
                        level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.LAVA) || level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.LAVA_CAULDRON)) {
                    player.awardStat(Stats.ITEM_USED.get(Items.STICK));
                    cir.setReturnValue(InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.TORCH)), level.isClientSide()));
                } else if (level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.SOUL_CAMPFIRE) || level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.SOUL_FIRE)) {
                    player.awardStat(Stats.ITEM_USED.get(Items.STICK));
                    cir.setReturnValue(InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.SOUL_TORCH)), level.isClientSide()));
                }
            }
        } else if (itemStack.is(Items.BONE_MEAL)) {
            if (blockhitresult.getType().equals(HitResult.Type.BLOCK)) {
                BlockState blockState = level.getBlockState(blockhitresult.getBlockPos());
                if ((blockState.is(BlockTags.FLOWERS) || blockState.is(BlockTags.SMALL_FLOWERS)) && !blockState.is(BlockTags.TALL_FLOWERS)) {
                    player.awardStat(Stats.ITEM_USED.get(Items.BONE_MEAL));
                    cir.setReturnValue(InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(itemStack, player, new ItemStack(blockState.getBlock().asItem(), (int) (Math.random()*2)+1)), level.isClientSide()));
                }
            }
        }
    }
}
