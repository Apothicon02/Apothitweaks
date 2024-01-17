package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(EnchantmentTableBlock.class)
public class EnchantmentTableMixin {
    @Unique
    private Enchantment apothitweaks$boundEnchantment = Enchantments.VANISHING_CURSE;
    @Unique
    private int apothitweaks$boundPower = 1;
    
    /**
     * @author Apothicon
     * @reason Binds the enchantment table to an enchantment.
     */
    @Overwrite
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult block) {
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
            int highestPower = apothitweaks$boundPower;
            Enchantment highestEnchant = apothitweaks$boundEnchantment;
            Map<Enchantment, Integer> allEnchants = EnchantmentHelper.getEnchantments(book);
            for (Map.Entry<Enchantment, Integer> enchantment : allEnchants.entrySet()) {
                int power = enchantment.getValue();
                Enchantment enchant = enchantment.getKey();
                if (power >= highestPower) {
                    highestPower = power;
                    boolean isSameEnchant = enchant == apothitweaks$boundEnchantment;
                    if (isSameEnchant || apothitweaks$boundEnchantment == Enchantments.VANISHING_CURSE) {
                        highestEnchant = enchant;
                        if (isSameEnchant) {
                            break;
                        }
                    }
                }
            }
            if (lapis.getCount() >= 32 && apothitweaks$boundEnchantment.equals(Enchantments.VANISHING_CURSE)) {
                book.setCount(book.getCount() - 1);
                lapis.setCount((int) (lapis.getCount() - Math.max(Math.random() * 32, 16)));
                apothitweaks$boundPower = highestPower;
                apothitweaks$boundEnchantment = highestEnchant;
                level.playSound(player, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS);
            } else if (lapis.getCount() >= 16 && apothitweaks$boundEnchantment.equals(highestEnchant) && apothitweaks$boundPower <= highestPower) {
                book.setCount(book.getCount() - 1);
                lapis.setCount((int) (lapis.getCount() - Math.max(Math.random() * 16, 8)));
                if (apothitweaks$boundPower == highestPower) {
                    apothitweaks$boundPower = highestPower+1;
                } else {
                    apothitweaks$boundPower = highestPower;
                }
                apothitweaks$boundEnchantment = highestEnchant;
                level.playSound(player, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS);
            } else {
                level.playSound(player, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
            }
        } else if (itemInHand.canApplyAtEnchantingTable(apothitweaks$boundEnchantment) && lapis != null) {
            if (lapis.getCount() >= 16) {
                lapis.setCount((int) (lapis.getCount() - Math.max(Math.random() * 16, 8)));
                itemInHand.enchant(apothitweaks$boundEnchantment, apothitweaks$boundPower);
                level.playSound(player, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS);
            } else {
                level.playSound(player, pos, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS);
            }
        } else {
            level.playSound(player, pos, SoundEvents.ENDERMITE_STEP, SoundSource.BLOCKS);
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }
}