package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    /**
     * @author Apothicon
     * @reason Prevents items from being pre-enchanted with anything other than unbreaking.
     */
    @Overwrite
    public static List<EnchantmentInstance> selectEnchantment(RandomSource p_220298_, ItemStack p_220299_, int p_220300_, boolean p_220301_) {
        if (p_220299_.is(Items.BOOK) || p_220299_.is(Items.ENCHANTED_BOOK)) {
            return List.of();
        } else {
            return List.of(new EnchantmentInstance(Enchantments.UNBREAKING, (int) (Math.random() * 2) + 1));
        }
    }
}
