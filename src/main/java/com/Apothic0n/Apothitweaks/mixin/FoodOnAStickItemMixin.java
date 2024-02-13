package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FoodOnAStickItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodOnAStickItem.class)
public class FoodOnAStickItemMixin<T extends Entity & ItemSteerable> extends Item {
    @Shadow @Final private EntityType<T> canInteractWith;

    @Shadow @Final private int consumeItemDamage;

    public FoodOnAStickItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    /**
     * @author Apothicon
     * @reason Makes food on a stick items automatically replace themselves if the food is in the inventory. 
     */
    @Overwrite
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Entity entity = player.getControlledVehicle();
            if (player.isPassenger() && entity instanceof ItemSteerable) {
                ItemSteerable itemsteerable = (ItemSteerable)entity;
                if (entity.getType() == this.canInteractWith && itemsteerable.boost()) {
                    boolean wasCarrotOnAStick = itemstack.is(Items.CARROT_ON_A_STICK);
                    boolean wasFungusOnAStick = itemstack.is(Items.WARPED_FUNGUS_ON_A_STICK);
                    itemstack.hurtAndBreak(this.consumeItemDamage, player, (p_41312_) -> {
                        p_41312_.broadcastBreakEvent(hand);
                    });
                    if (itemstack.isEmpty()) {
                        ItemStack itemstack1 = new ItemStack(Items.FISHING_ROD);
                        Inventory inventory = player.getInventory();
                        int carrots = inventory.findSlotMatchingItem(Items.CARROT.getDefaultInstance());
                        int fungus = inventory.findSlotMatchingItem(Items.WARPED_FUNGUS.getDefaultInstance());
                        if (wasCarrotOnAStick && carrots != -1) {
                            ItemStack carrotStack = inventory.getItem(carrots);
                            carrotStack.setCount(carrotStack.getCount()-1);
                            inventory.setItem(carrots, carrotStack);
                            itemstack1 = new ItemStack(Items.CARROT_ON_A_STICK);
                        } else if (wasFungusOnAStick && fungus != -1) {
                            ItemStack fungusStack = inventory.getItem(fungus);
                            fungusStack.setCount(fungusStack.getCount()-1);
                            inventory.setItem(fungus, fungusStack);
                            itemstack1 = new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK);
                        }
                        itemstack1.setTag(itemstack.getTag());
                        return InteractionResultHolder.success(itemstack1);
                    }

                    return InteractionResultHolder.success(itemstack);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.pass(itemstack);
        }
    }
}
