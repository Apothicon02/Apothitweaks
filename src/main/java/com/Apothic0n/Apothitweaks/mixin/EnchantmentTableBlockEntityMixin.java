package com.Apothic0n.Apothitweaks.mixin;

import com.Apothic0n.Apothitweaks.Apothitweaks;
import com.Apothic0n.Apothitweaks.core.objects.EnchantmentTableInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EnchantmentTableBlockEntity.class)
public class EnchantmentTableBlockEntityMixin extends BlockEntity implements EnchantmentTableInterface {
    public EnchantmentTableBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Unique
    public Enchantment apothitweaks$boundEnchantment = Enchantments.VANISHING_CURSE;
    @Unique
    public int apothitweaks$boundPower = 1;

    @Override
    public int apothitweaks$getBoundPower() {
        return apothitweaks$boundPower;
    }

    @Override
    public void apothitweaks$setBoundPower(int newBoundPower) {
        apothitweaks$boundPower = newBoundPower;
    }

    @Override
    public Enchantment apothitweaks$getBoundEnchantment() {
        return apothitweaks$boundEnchantment;
    }

    @Override
    public void apothitweaks$setBoundEnchantment(Enchantment newBoundEnchantment) {
        apothitweaks$boundEnchantment = newBoundEnchantment;
        setChanged();
    }

    /**
     * @author Apothicon
     * @reason No need to save the normal data anymore, instead this will save the new data.
     */
    @Overwrite
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        var tag = new CompoundTag();
        tag.putString("boundEnchantment", apothitweaks$getBoundEnchantment().getDescriptionId());
        tag.putInt("boundPower", apothitweaks$getBoundPower());
        nbt.put(Apothitweaks.MODID, tag);
    }

    /**
     * @author Apothicon
     * @reason No need to load the normal data anymore, instead this will load the new data.
     */
    @Overwrite
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        CompoundTag tag = nbt.getCompound(Apothitweaks.MODID);
        String descId = tag.getString("boundEnchantment");
        Enchantment enchant = Enchantments.VANISHING_CURSE;
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
            if (enchantment.getDescriptionId().equals(descId)) {
                enchant = enchantment;
                break;
            }
        }
        apothitweaks$setBoundEnchantment(enchant);
        apothitweaks$setBoundPower(tag.getInt("boundPower"));
    }
}