package com.Apothic0n.Apothitweaks.core.objects;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface ApothitweaksPacket {
    void encode(FriendlyByteBuf buffer);
    void receiveMessage(Supplier<NetworkEvent.Context> context);
}