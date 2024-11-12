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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.DensityFunction;
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
                    float distance = event.getNearPlaneDistance() / getTimeOffset(level, 32, 3);
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
                float temp = level.getBiome(event.getCamera().getBlockPosition()).get().getBaseTemperature() * 3;
                float brightness = (getTimeOffset(level, 5, 0)/10) + ((Math.max(-0.75f, Math.min(-0.7f, temp/3))+0.7f)*getTimeOffsetInv(level, 5f, 0));
                event.setRed(Math.min(0.95f, 0.7f - brightness));
                event.setGreen(Math.min(0.95f, 0.8f - brightness - (Math.max((Math.max(0.4f, Math.min(0.5f, temp/3))-0.4f)*0.66f, getTimeOffset(level, 0.2f, 0)))));
                event.setBlue(Math.min(1, 1 - brightness - (((Math.max(0.3f, Math.min(0.5f, temp/3))-0.4f)*0.66f)*getTimeOffsetInv(level, 5f, 0))));
            } else if (level != null && event.getCamera().getFluidInCamera() == FogType.WATER) {
                event.setRed((float) Mth.clamp((ApothitweaksMath.getMiddleDouble(event.getRed(), 0.025) / 15), 0.025, 0.05));
                event.setGreen((float) Mth.clamp((ApothitweaksMath.getMiddleDouble(event.getGreen(), 0.175) / 15), 0.175, 0.35));
                event.setBlue((float) Mth.clamp((ApothitweaksMath.getMiddleDouble(event.getBlue(), 0.175) / 15), 0.175, 0.35));
            }
        }
    }

    private static float getTimeOffset(ClientLevel level, float scale, int outOfBounds) {
        float offset = outOfBounds;
        long dayTime = level.getDayTime();
        if (dayTime >= 11800 && dayTime <= 13000) { //dusk
            offset += ApothitweaksMath.invLerp(dayTime, scale, 11800, 13000);
        } else if (dayTime > 13000 && dayTime < 22000) { //night
            offset += scale;
        } else if (dayTime >= 22000 && dayTime <= 23500) { //dawn
            offset += ApothitweaksMath.invLerp(dayTime, scale, 22000, 23500);
        }
        return offset; //day
    }

    private static float getTimeOffsetInv(ClientLevel level, float scale, int outOfBounds) {
        float offset = outOfBounds;
        long dayTime = level.getDayTime();
        if (dayTime >= 11800 && dayTime <= 13000) { //dusk
            offset += ApothitweaksMath.invLerp(dayTime, scale, 13000, 11800);
        } else if (dayTime < 11800 || dayTime > 23500) { //day
            offset += scale;
        } else if (dayTime >= 22000) { //dawn
            offset += ApothitweaksMath.invLerp(dayTime, scale, 23500, 22000);
        }
        return offset; //night
    }
}
