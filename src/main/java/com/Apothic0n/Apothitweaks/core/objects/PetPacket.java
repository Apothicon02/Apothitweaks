package com.Apothic0n.Apothitweaks.core.objects;

import com.Apothic0n.Apothitweaks.api.ApothitweaksJsonReader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PetPacket implements ApothitweaksPacket {
    private final short data;

    public PetPacket(final short data) {
        this.data = data;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeShort(data);
    }

    public static PetPacket decode(FriendlyByteBuf buffer) {
        return new PetPacket(buffer.readShort());
    }

    public void receiveMessage(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (ApothitweaksJsonReader.config.contains("mounts")) {
                NetworkEvent.Context data = context.get();
                Player player = data.getSender();
                if (player != null && !player.isSpectator()) {
                    player.getCapability(PlayerPetProvider.PLAYER_PET).ifPresent(playerPet -> {
                        BlockPos pos = player.blockPosition();
                        ServerLevel level = (ServerLevel) player.level();
                        if (level.hasChunkAt(pos)) {
                            CompoundTag pet = playerPet.getPet();
                            if (pet.isEmpty()) {
                                Entity entity = player.getControlledVehicle();
                                if (entity != null) {
                                    pet = entity.serializeNBT();
                                    playerPet.setPet(pet);
                                    entity.remove(Entity.RemovalReason.DISCARDED);
                                    level.playSound(null, pos, SoundEvents.SNIFFER_EGG_PLOP, SoundSource.PLAYERS, 1F, 1F);
                                }
                            } else {
                                Entity entity = EntityType.create(pet, level).orElseThrow();
                                entity.moveTo(player.position());
                                entity.resetFallDistance();
                                level.addFreshEntity(entity);
                                playerPet.setPet(new CompoundTag());
                                player.startRiding(entity);
                                level.playSound(null, pos, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.PLAYERS, 1F, 1F);
                            }
                        }
                    });
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
