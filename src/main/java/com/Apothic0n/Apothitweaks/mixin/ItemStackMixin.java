package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<ItemStack> {

    protected ItemStackMixin(Class<ItemStack> baseClass) {
        super(baseClass);
    }

    @Shadow public abstract Item getItem();

    /**
     * @author Apothicon
     * @reason Makes all tools except wood, stone, and iron effectively have max efficiency at all times.
     */
    @Overwrite
    public float getDestroySpeed(BlockState blockState) {
        ItemStack itemStack = (ItemStack) (Object) this;
        Item item = itemStack.getItem();
        float speed = item.getDestroySpeed(itemStack, blockState);
        if (speed > 1.0F && item instanceof TieredItem tieredItem) {
            if (!tieredItem.getTier().equals(Tiers.WOOD) && !tieredItem.getTier().equals(Tiers.STONE) && !tieredItem.getTier().equals(Tiers.IRON)) {
                speed += 5 * 5 + 1;
            }
        }
        return speed;
    }
}
