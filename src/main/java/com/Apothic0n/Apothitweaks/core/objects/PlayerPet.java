package com.Apothic0n.Apothitweaks.core.objects;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class PlayerPet {
    private CompoundTag pet;

    public CompoundTag getPet() {
        return pet;
    }

    public void setPet(CompoundTag petNbt) {
        this.pet = petNbt;
    }

    public void copyFrom(PlayerPet source) {
        this.pet = source.pet;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (pet != null) {
            nbt.put("pet", pet);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        pet = nbt.getCompound("pet");
    }
}
