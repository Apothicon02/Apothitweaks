package com.Apothic0n.Apothitweaks.core.events;

import com.Apothic0n.Apothitweaks.Apothitweaks;
import com.Apothic0n.Apothitweaks.api.ApothitweaksJsonReader;
import com.Apothic0n.Apothitweaks.core.ApothitweaksMath;
import com.Apothic0n.Apothitweaks.core.objects.ApothitweaksPacketHandler;
import com.Apothic0n.Apothitweaks.core.objects.PetPacket;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import static com.Apothic0n.Apothitweaks.core.ApothitweaksMath.getMiddleDouble;
import static com.Apothic0n.Apothitweaks.core.events.ClientModEvents.PET_MAPPING;

@Mod.EventBusSubscriber(modid = Apothitweaks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    static void inputKeyEvent(InputEvent.Key event) {
        if (ApothitweaksJsonReader.config.contains("mounts") && event.getAction() == InputConstants.RELEASE && event.getKey() == PET_MAPPING.get().getKey().getValue() && Minecraft.getInstance().screen == null) {
            ApothitweaksPacketHandler.messageServer(new PetPacket((short) 69));
        }
    }

    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        if (ApothitweaksJsonReader.config.contains("custom_fog")) {
            Minecraft instance = Minecraft.getInstance();
            ClientLevel level = instance.level;
            if (event.getType() == FogType.LAVA) {
                event.setNearPlaneDistance(1);
                event.setFarPlaneDistance(event.getFarPlaneDistance() * 15);
                event.setFogShape(FogShape.SPHERE);
                event.setCanceled(true);
            } else if (event.getType() == FogType.NONE && level != null) {
                if (level.dimension().location().toString().contains("nether")) {
                    event.setNearPlaneDistance(1);
                    event.setFarPlaneDistance(event.getFarPlaneDistance() * 3);
                    event.setFogShape(FogShape.SPHERE);
                    event.setCanceled(true);
                } else if (level.dimension().location().toString().contains("overworld") && !ModList.get().isLoaded("hydrol")) {
                    float distance = event.getNearPlaneDistance() / getTimeOffset(level, 32);
                    float y = (float) event.getCamera().getPosition().y();
                    if (y < 48) {
                        event.setFarPlaneDistance(event.getFarPlaneDistance() / (ApothitweaksMath.invLerp(y, 1, 48, 16) + 1));
                        distance = distance / (ApothitweaksMath.invLerp(y, 32, 48, 16) + 1);
                    }
                    event.setNearPlaneDistance(distance);
                    event.setFogShape(FogShape.SPHERE);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void computeFogColor(ViewportEvent.ComputeFogColor event) {
        if (ApothitweaksJsonReader.config.contains("custom_fog")) {
            Minecraft instance = Minecraft.getInstance();
            ClientLevel level = instance.level;
            if (level != null && level.dimension().location().toString().contains("overworld") && event.getCamera().getFluidInCamera() == FogType.NONE && !ModList.get().isLoaded("hydrol")) {
                float y = (float) event.getCamera().getPosition().y();
                float temp = level.getBiome(event.getCamera().getBlockPosition().atY(69)).get().getBaseTemperature();
                event.setRed(event.getRed() + (((temp - 0.8F) / 25)));
                event.setGreen(event.getGreen() - (((temp - 0.8F) / 20)));
                event.setBlue(event.getBlue() - (((temp - 0.8F) / 15)));
                if (y < 48) {
                    float yScale = ApothitweaksMath.invLerp(Math.min(Math.max(y, 16), 48), 1, 48, 16);
                    float invYScale = ApothitweaksMath.invLerp(Math.min(Math.max(y, 16), 48), 0.8F, 16, 48);
                    event.setRed((Math.max(yScale, event.getRed()) - (Math.min(yScale, event.getRed()) * yScale) + Math.min(yScale, event.getRed())) * (invYScale + 0.2F));
                    event.setGreen((Math.max(yScale, event.getGreen()) - (Math.min(yScale, event.getGreen()) * yScale) + Math.min(yScale, event.getGreen())) * (invYScale + 0.2F));
                    event.setBlue((Math.max(yScale, event.getBlue()) - (Math.min(yScale, event.getBlue()) * yScale) + Math.min(yScale, event.getBlue())) * (invYScale + 0.2F));
                }
            } else if (event.getCamera().getFluidInCamera() == FogType.WATER) {
                event.setRed((float) getMiddleDouble(event.getRed(), 0.025));
                event.setGreen((float) getMiddleDouble(event.getGreen(), 0.175));
                event.setBlue((float) getMiddleDouble(event.getBlue(), 0.175));
            }
        }
    }

    private static float getTimeOffset(ClientLevel level, int scale) {
        float offset = 3;
        long dayTime = level.getDayTime();
        if (dayTime >= 11800 && dayTime <= 13000) { //dusk
            offset += ApothitweaksMath.invLerp(dayTime, scale, 11800, 13000);
        } else if (dayTime > 13000 && dayTime < 22000) { //night
            offset += scale;
        } else if (dayTime >= 22000 && dayTime <= 23500) { //dawn
            offset += ApothitweaksMath.invLerp(dayTime, scale, 22000, 23500);
        }
        return offset;
    }
}
