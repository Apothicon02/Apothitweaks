package com.Apothic0n.Apothitweaks.core.events;

import com.Apothic0n.Apothitweaks.Apothitweaks;
import com.Apothic0n.Apothitweaks.api.ApothitweaksJsonReader;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Apothitweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    public static final Lazy<KeyMapping> PET_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.apothitweaks.pet",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.gameplay"
    ));
    @SubscribeEvent
    public static void registerKeyMappingsEvent(RegisterKeyMappingsEvent event) {
        if (ApothitweaksJsonReader.config.contains("mounts")) {
            event.register(PET_MAPPING.get());
        }
    }
}
