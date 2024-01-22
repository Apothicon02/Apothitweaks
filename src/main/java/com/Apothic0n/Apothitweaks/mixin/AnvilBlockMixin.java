package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {

    @Shadow
    @Nullable
    public static BlockState damage(BlockState p_48825_) {
        return null;
    }

    /**
     * @author Apothicon
     * @reason Custom anvil system.
     */
    @Overwrite
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            MutableComponent feedback;
            ItemStack itemInHand = player.getMainHandItem();
            ItemStack itemInOffhand = player.getOffhandItem();
            ItemStack gear = null;
            ItemStack resource = null;
            ItemStack nameTag = null;
            if (itemInHand.is(Items.NAME_TAG)) {
                nameTag = itemInHand;
            } else if (itemInOffhand.is(Items.NAME_TAG)) {
                nameTag = itemInOffhand;
            }
            if (nameTag != null) {
                Component newName = nameTag.getHoverName();
                if (!itemInHand.is(Items.NAME_TAG)) {
                    gear = itemInHand;
                } else if (!itemInOffhand.is(Items.NAME_TAG)) {
                    gear = itemInOffhand;
                }
                if (gear != null && !gear.isEmpty()) {
                    String gearName = gear.getHoverName().getString();
                    if (player.experienceLevel >= 1) {
                        player.giveExperienceLevels(-1);
                        nameTag.setCount(nameTag.getCount()-1);
                        gear.setHoverName(newName);
                        player.awardStat(Stats.INTERACT_WITH_ANVIL);
                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.anvil.successfully_renamed", gearName, newName);
                    } else {
                        level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.anvil.failed_rename_exp", 1, gearName, newName);
                    }
                } else {
                    level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS);
                    feedback = Component.translatable("block.minecraft.anvil.failed_rename_item", newName);
                }
            } else {
                if (itemInHand.isRepairable()) {
                    gear = itemInHand;
                } else if (itemInOffhand.isRepairable()) {
                    gear = itemInOffhand;
                }
                if (gear != null && !gear.isEmpty()) {
                    String gearName = gear.getHoverName().getString();
                    if (itemInHand.isRepairable()) {
                        resource = itemInOffhand;
                    } else if (itemInOffhand.isRepairable()) {
                        resource = itemInHand;
                    }
                    if (resource != null && !resource.isEmpty()) {
                        String resourceName = resource.getHoverName().getString();
                        if (gear.getItem().isValidRepairItem(gear, resource) || ((gearName.contains("Netherite") || gearName.contains("netherite")) && resource.is(Items.DIAMOND))) {
                            if (player.experienceLevel >= 3) {
                                if (gear.getDamageValue() > 0) {
                                    player.giveExperienceLevels(-3);
                                    resource.setCount(resource.getCount() - 1);
                                    gear.setDamageValue(-gear.getMaxDamage());
                                    if (Math.random() * 100 < 12) {
                                        BlockState damagedState = damage(state);
                                        if (damagedState != null) {
                                            level.setBlock(pos, damagedState, UPDATE_ALL);
                                        } else {
                                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_ALL);
                                        }
                                    }
                                    player.awardStat(Stats.INTERACT_WITH_ANVIL);
                                    level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
                                    feedback = Component.translatable("block.minecraft.anvil.successfully_repaired", gearName, resourceName);
                                } else {
                                    level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS);
                                    feedback = Component.translatable("block.minecraft.anvil.failed_repair_maxed", gearName);
                                }
                            } else {
                                level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS);
                                feedback = Component.translatable("block.minecraft.anvil.failed_repair_exp", 3-player.experienceLevel, gearName);
                            }
                        } else {
                            level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS);
                            feedback = Component.translatable("block.minecraft.anvil.invalid_resource", resourceName, gearName);
                        }
                    } else {
                        level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.anvil.no_resource", gearName);
                    }
                } else {
                    String mainHandItemName = "";
                    if (!itemInHand.isEmpty()) {
                        mainHandItemName = itemInHand.getHoverName().getString();
                    }
                    String offhandItemName = "";
                    if (!itemInOffhand.isEmpty()) {
                        offhandItemName = itemInOffhand.getHoverName().getString();
                    }
                    level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS);
                    if (!mainHandItemName.isEmpty() && !mainHandItemName.equals("Air") && !offhandItemName.isEmpty() && !offhandItemName.equals("Air")) {
                        feedback = Component.translatable("block.minecraft.anvil.unrepairable", mainHandItemName, offhandItemName);
                    } else if (!mainHandItemName.isEmpty() && !mainHandItemName.equals("Air")) {
                        feedback = Component.translatable("block.minecraft.anvil.single_unrepairable", mainHandItemName);
                    } else if (!offhandItemName.isEmpty() && !offhandItemName.equals("Air")) {
                        feedback = Component.translatable("block.minecraft.anvil.single_unrepairable", offhandItemName);
                    } else {
                        feedback = Component.translatable("block.minecraft.anvil.nothing");
                    }
                }
            }
            player.displayClientMessage(feedback, true);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }
}
