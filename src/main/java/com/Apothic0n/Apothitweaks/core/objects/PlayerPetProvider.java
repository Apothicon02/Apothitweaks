package com.Apothic0n.Apothitweaks.core.objects;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerPetProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerPet> PLAYER_PET = CapabilityManager.get(new CapabilityToken<PlayerPet>() { });
    private PlayerPet playerPet = null;
    private final LazyOptional<PlayerPet> optional = LazyOptional.of(this::createPlayerPet);

    private PlayerPet createPlayerPet() {
        if (this.playerPet == null) {
            this.playerPet = new PlayerPet();
        }
        return this.playerPet;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_PET) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerPet().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerPet().loadNBTData(nbt);
    }
}
