package com.Apothic0n.Apothitweaks.mixin;

import com.Apothic0n.Apothitweaks.core.objects.RenameTagScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NameTagItem.class)
public class NameTagItemMixin extends Item {
    public NameTagItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level.isClientSide && hand.equals(InteractionHand.OFF_HAND)) {
            Minecraft.getInstance().setScreen(new RenameTagScreen(itemStack));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
