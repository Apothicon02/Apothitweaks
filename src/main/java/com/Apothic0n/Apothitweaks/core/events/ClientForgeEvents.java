package com.Apothic0n.Apothitweaks.core.events;

import com.Apothic0n.Apothitweaks.Apothitweaks;
import com.Apothic0n.Apothitweaks.core.objects.ApothitweaksPacketHandler;
import com.Apothic0n.Apothitweaks.core.objects.PetPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.Apothic0n.Apothitweaks.core.events.ClientModEvents.PET_MAPPING;

@Mod.EventBusSubscriber(modid = Apothitweaks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    static void inputKeyEvent(InputEvent.Key event) {
        if (event.getAction() == InputConstants.RELEASE && event.getKey() == PET_MAPPING.get().getKey().getValue() && Minecraft.getInstance().screen == null) {
            ApothitweaksPacketHandler.messageServer(new PetPacket((short) 69));
        }
    }
}
