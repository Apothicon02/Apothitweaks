package com.Apothic0n.Apothitweaks;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Apothitweaks.MODID)
public class Apothitweaks {
    public static final String MODID = "apothitweaks";

    public Apothitweaks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}