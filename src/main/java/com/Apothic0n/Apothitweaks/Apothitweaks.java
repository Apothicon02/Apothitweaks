package com.Apothic0n.Apothitweaks;

import com.Apothic0n.Apothitweaks.api.ApothitweaksJsonReader;
import com.Apothic0n.Apothitweaks.api.biome.features.ApothitweaksFeatureRegistry;
import com.Apothic0n.Apothitweaks.core.objects.ApothitweaksPacketHandler;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
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
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ApothitweaksPacketHandler.init();
        ApothitweaksFeatureRegistry.register(eventBus);

        if (ApothitweaksJsonReader.config.contains("cartographer_sells_recovery_compass")) {
            VillagerTrades.TRADES.get(VillagerProfession.CARTOGRAPHER).put(1, new VillagerTrades.ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.RECOVERY_COMPASS, 3, 1, 2)});
        }
        if (ApothitweaksJsonReader.config.contains("stackable_potions")) {
            ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.POTION, 16, "f_41370_");
        }
    }
}