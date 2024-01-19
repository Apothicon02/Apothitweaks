package com.Apothic0n.Apothitweaks.mixin;

import com.Apothic0n.Apothitweaks.core.objects.EnchantmentTableInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.Map;

@Mixin(EnchantmentTableBlock.class)
public class EnchantmentTableMixin extends BaseEntityBlock {
    protected EnchantmentTableMixin(Properties properties) {
        super(properties);
    }

    /**
     * @author Apothicon
     * @reason Binds the enchantment table to an enchantment.
     */
    @Overwrite
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult block) {
        if (!level.isClientSide) {
            MutableComponent feedback = Component.translatable("block.minecraft.enchantment_table.default");
            BlockEntity genericBlockEntity = level.getBlockEntity(pos);
            if (genericBlockEntity instanceof EnchantmentTableInterface blockEntity) {
                int amountOfShelves = 0;
                for (BlockState state : level.getBlockStates(new AABB(pos.north(2).east(2), pos.south(2).west(2).above())).toList()) {
                    if (state.is(BlockTags.ENCHANTMENT_POWER_PROVIDER)) {
                        amountOfShelves++;
                    }
                }
                if (amountOfShelves >= 6) {
                    int boundPower = blockEntity.apothitweaks$getBoundPower();
                    Enchantment boundEnchantment = blockEntity.apothitweaks$getBoundEnchantment();
                    ItemStack itemInHand = player.getMainHandItem();
                    ItemStack itemInOffhand = player.getOffhandItem();
                    ItemStack lapis = null;
                    ItemStack book = null;
                    if (itemInHand.is(Items.ENCHANTED_BOOK)) {
                        book = itemInHand;
                    } else if (itemInOffhand.is(Items.ENCHANTED_BOOK)) {
                        book = itemInOffhand;
                    }
                    if (itemInHand.is(Items.LAPIS_LAZULI)) {
                        lapis = itemInHand;
                    } else if (itemInOffhand.is(Items.LAPIS_LAZULI)) {
                        lapis = itemInOffhand;
                    }
                    if (book != null && lapis != null) {
                        int highestPower = 1;
                        Enchantment highestEnchant = boundEnchantment;
                        Map<Enchantment, Integer> allEnchants = EnchantmentHelper.getEnchantments(book);
                        for (Map.Entry<Enchantment, Integer> enchantment : allEnchants.entrySet()) {
                            int power = enchantment.getValue();
                            Enchantment enchant = enchantment.getKey();
                            if (power >= highestPower) {
                                highestPower = power;
                                boolean isSameEnchant = enchant == boundEnchantment;
                                if (isSameEnchant || boundEnchantment == Enchantments.VANISHING_CURSE) {
                                    highestEnchant = enchant;
                                    if (isSameEnchant) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (player.experienceLevel >= highestPower) {
                            if (boundEnchantment.equals(Enchantments.VANISHING_CURSE)) {
                                if (lapis.getCount() >= 32) {
                                    book.setCount(book.getCount() - 1);
                                    lapis.setCount((int) (lapis.getCount() - Math.max(Math.random() * 32, 16)));
                                    player.giveExperienceLevels(-highestPower);
                                    blockEntity.apothitweaks$setBoundPower(highestPower);
                                    blockEntity.apothitweaks$setBoundEnchantment(highestEnchant);
                                    level.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS);
                                    feedback = Component.translatable("block.minecraft.enchantment_table.successfully_bound");
                                } else if (lapis.getCount() < 32) {
                                    level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                                    feedback = Component.translatable("block.minecraft.enchantment_table.failed_bound_lapis");
                                }
                            } else {
                                if (lapis.getCount() >= 16 && highestPower < highestEnchant.getMaxLevel() && boundPower <= highestPower) {
                                    if (boundPower == highestPower) {
                                        book.setCount(book.getCount() - 1);
                                        lapis.setCount((int) (lapis.getCount() - Math.max(Math.random() * 16, 8)));
                                        player.giveExperienceLevels(-highestPower);
                                        blockEntity.apothitweaks$setBoundPower(highestPower + 1);
                                        blockEntity.apothitweaks$setBoundEnchantment(highestEnchant);
                                        level.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS);
                                        feedback = Component.translatable("block.minecraft.enchantment_table.successfully_powered_up");
                                    } else {
                                        book.setCount(book.getCount() - 1);
                                        lapis.setCount((int) (lapis.getCount() - Math.max(Math.random() * 16, 8)));
                                        player.giveExperienceLevels(-highestPower);
                                        blockEntity.apothitweaks$setBoundPower(highestPower);
                                        blockEntity.apothitweaks$setBoundEnchantment(highestEnchant);
                                        level.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS);
                                        feedback = Component.translatable("block.minecraft.enchantment_table.successfully_powered_up");
                                    }
                                } else if (lapis.getCount() < 16) {
                                    level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                                    feedback = Component.translatable("block.minecraft.enchantment_table.failed_bound_lapis");
                                } else if (highestPower >= highestEnchant.getMaxLevel()) {
                                    level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                                    feedback = Component.translatable("block.minecraft.enchantment_table.failed_bound_maxed");
                                } else if (boundPower > highestPower) {
                                    level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                                    feedback = Component.translatable("block.minecraft.enchantment_table.failed_bound_weaker");
                                }
                            }
                        } else {
                            level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                            feedback = Component.translatable("block.minecraft.enchantment_table.failed_bound_exp");
                        }
                    } else if (itemInHand.canApplyAtEnchantingTable(boundEnchantment) && lapis != null && itemInHand.getEnchantmentLevel(boundEnchantment) < boundPower) {
                        String name = itemInHand.getHoverName().getString();
                        Map<Enchantment, Integer> newEnchantments = itemInHand.getAllEnchantments();
                        Integer didReplace = newEnchantments.replace(boundEnchantment, boundPower);
                        int totalEnchantments = newEnchantments.size();
                        if (didReplace == null) {
                            totalEnchantments++;
                        }
                        int cappedLapisCost = Math.min(16*totalEnchantments, 64);
                        int expCost = 2*totalEnchantments;
                        if (lapis.getCount() >= cappedLapisCost && player.experienceLevel >= expCost) {
                            lapis.setCount((int) (lapis.getCount() - Math.max(Math.random() * cappedLapisCost, cappedLapisCost-8)));
                            player.giveExperienceLevels(-expCost);
                            if (didReplace == null) {
                                itemInHand.enchant(boundEnchantment, boundPower);
                            } else {
                                EnchantmentHelper.setEnchantments(newEnchantments, itemInHand);
                            }
                            if (boundEnchantment.equals(Enchantments.VANISHING_CURSE)) {
                                feedback = Component.translatable("block.minecraft.enchantment_table.successfully_cursed", name+".");
                            } else {
                                feedback = Component.translatable("block.minecraft.enchantment_table.successfully_enchanted", name);
                            }
                            level.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS);
                        } else if (lapis.getCount() < cappedLapisCost) {
                            level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                            feedback = Component.translatable("block.minecraft.enchantment_table.failed_enchant_lapis", name+".");
                        } else if (player.experienceLevel < expCost) {
                            level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                            feedback = Component.translatable("block.minecraft.enchantment_table.failed_enchant_exp", name+".");
                        } else if (itemInHand.getEnchantmentLevel(boundEnchantment) == boundPower) {
                            level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                            feedback = Component.translatable("block.minecraft.enchantment_table.failed_enchant_existing", name);
                        } else if (itemInHand.getEnchantmentLevel(boundEnchantment) > boundPower) {
                            level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                            feedback = Component.translatable("block.minecraft.enchantment_table.failed_enchant_weaker", name);
                        }
                    } else if (lapis == null) {
                        level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.enchantment_table.hold_lapis");
                    } else if (itemInHand.getEnchantmentLevel(boundEnchantment) == boundPower) {
                        level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.enchantment_table.existing");
                    } else if (itemInHand.getEnchantmentLevel(boundEnchantment) > boundPower) {
                        level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.enchantment_table.weaker");
                    } else if (!itemInHand.canApplyAtEnchantingTable(boundEnchantment)) {
                        level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.enchantment_table.unsupported");
                    } else {
                        level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                        feedback = Component.translatable("block.minecraft.enchantment_table.empty_handed");
                    }
                } else {
                    level.playSound(null, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
                    feedback = Component.translatable("block.minecraft.enchantment_table.bookshelves", 6-amountOfShelves);
                }
            }
            player.displayClientMessage(feedback, true);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EnchantmentTableBlockEntity(pos, state);
    }
}