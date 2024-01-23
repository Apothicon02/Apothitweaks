package com.Apothic0n.Apothitweaks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
@Mod(Apothitweaks.MODID)
public class Apothitweaks {
    public static final String MODID = "apothitweaks";

    public Apothitweaks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.POTION, 16, "f_41370_");
        ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.COOKED_BEEF, 1, "f_41370_");
    }
}