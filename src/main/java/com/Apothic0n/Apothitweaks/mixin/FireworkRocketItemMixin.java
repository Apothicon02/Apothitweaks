package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    /**
     * @author Apothicon
     * @reason Disables elytra boosting.
     */
    @Overwrite
    public InteractionResultHolder<ItemStack> use(Level p_41218_, Player p_41219_, InteractionHand p_41220_) {
        return InteractionResultHolder.pass(p_41219_.getItemInHand(p_41220_));
    }
}
