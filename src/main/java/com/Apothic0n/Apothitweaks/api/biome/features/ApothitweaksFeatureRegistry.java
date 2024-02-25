package com.Apothic0n.Apothitweaks.api.biome.features;

import com.Apothic0n.Apothitweaks.Apothitweaks;
import com.Apothic0n.Apothitweaks.api.biome.features.types.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ApothitweaksFeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Apothitweaks.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PATH_FEATURE = FEATURES.register("path", () ->
            new PathFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> NOODLE_CAVE_FEATURE = FEATURES.register("noodle_cave", () ->
            new NoodleCaveFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> NOODLE_RIVER_FEATURE = FEATURES.register("noodle_river", () ->
            new NoodleRiverFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
