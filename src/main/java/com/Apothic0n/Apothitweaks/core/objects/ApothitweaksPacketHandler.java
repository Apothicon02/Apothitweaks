package com.Apothic0n.Apothitweaks.core.objects;

import com.Apothic0n.Apothitweaks.Apothitweaks;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;


public class ApothitweaksPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Apothitweaks.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;

        INSTANCE.registerMessage(id++, PetPacket.class,
                PetPacket::encode,
                PetPacket::decode,
                PetPacket::receiveMessage);
    }

    public static void messageServer(ApothitweaksPacket packet) {
        INSTANCE.sendToServer(packet);
    }
}
