package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Shadow public abstract Component getFullname(int p_44701_);

    /**
     * @author Apothicon
     * @reason Makes librarians only able to trade throns, knockback, and punch books.
     */
    @Overwrite
    public boolean isTradeable() {
        String name = this.getFullname(1).toString();
        return name.contains("thorns") || name.contains("knockback") || name.contains("punch");
    }
}
