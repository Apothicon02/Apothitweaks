package com.Apothic0n.Apothitweaks.api;

import net.minecraftforge.fml.loading.FMLPaths;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ApothitweaksMixinConfig implements IMixinConfigPlugin {

    /**
     * Called after the plugin is instantiated, do any setup here.
     *
     * @param mixinPackage The mixin root package from the config
     */
    @Override
    public void onLoad(String mixinPackage) {
        try {
            ApothitweaksJsonReader.readConfig(Path.of(FMLPaths.CONFIGDIR.get().toString() + "/apothitweaks_config.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called only if the "referenceMap" key in the config is <b>not</b> set.
     * This allows the refmap file name to be supplied by the plugin
     * programatically if desired. Returning <code>null</code> will revert to
     * the default behaviour of using the default refmap json file.
     *
     * @return Path to the refmap resource or null to revert to the default
     */
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    /**
     * Called during mixin intialisation, allows this plugin to control whether
     * a specific will be applied to the specified target. Returning false will
     * remove the target from the mixin's target set, and if all targets are
     * removed then the mixin will not be applied at all.
     *
     * @param targetClassName Fully qualified class name of the target class
     * @param mixinClassName  Fully qualified class name of the mixin
     * @return True to allow the mixin to be applied, or false to remove it from
     * target's mixin set
     */
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String mixin = mixinClassName.substring(33, mixinClassName.length()-5);
        return switch (mixin) {
            case "Minecraft" -> ApothitweaksJsonReader.config.contains("instant_block_placement");
            case "EntityMinecartRotation" -> ApothitweaksJsonReader.config.contains("minecart_camera_fix");
            case "AbstractMinecart" -> ApothitweaksJsonReader.config.contains("faster_minecarts");
            case "AbstractSkeleton" -> ApothitweaksJsonReader.config.contains("inaccurate_skeletons");
            case "AnvilBlock" -> ApothitweaksJsonReader.config.contains("anvil_overhaul");
            case "Boat" -> ApothitweaksJsonReader.config.contains("faster_boats");
            case "BottleItem" -> ApothitweaksJsonReader.config.contains("pitcher_plant_fills_bottles");
            case "ClientLevel", "ServerLevel" -> ApothitweaksJsonReader.config.contains("slower_day_night_cycle");
            case "Creeper" -> ApothitweaksJsonReader.config.contains("creepers_explode_into_fire");
            case "EnchantmentHelper" -> ApothitweaksJsonReader.config.contains("only_unbreaking_on_loot");
            case "Enchantment" -> ApothitweaksJsonReader.config.contains("limited_librarian_enchants");
            case "EnchantmentTableBlockEntity", "EnchantmentTableBlock" ->
                    ApothitweaksJsonReader.config.contains("enchantment_table_overhaul");
            case "EntityFootsteps" -> ApothitweaksJsonReader.config.contains("louder_hostile_footsteps");
            case "FireworkRocketItem" -> ApothitweaksJsonReader.config.contains("no_elytra_boosting");
            case "FoodData" -> ApothitweaksJsonReader.config.contains("hunger_and_healing_overhaul");
            case "FoodOnAStickItem" -> ApothitweaksJsonReader.config.contains("food_on_stick_auto_recrafting");
            case "ItemEntity" -> ApothitweaksJsonReader.config.contains("longer_despawn_timer");
            case "Item" ->
                    ApothitweaksJsonReader.config.contains("sticks_convert_to_torches_on_fires_and_bonemealable_flowers");
            case "ItemStack" -> ApothitweaksJsonReader.config.contains("faster_high_end_tools");
            case "LivingEntity" -> ApothitweaksJsonReader.config.contains("poison_duration_nerf");
            case "NameTagItem", "ServerGamePacketListenerImpl" ->
                    ApothitweaksJsonReader.config.contains("name_tag_overhaul");
            case "Pig" -> ApothitweaksJsonReader.config.contains("faster_pig_riding");
            case "PlayerDrops" -> ApothitweaksJsonReader.config.contains("no_death_item_scattering");
            case "PlayerMovement" -> ApothitweaksJsonReader.config.contains("limited_sprinting");
            case "PlayerXP" -> ApothitweaksJsonReader.config.contains("flat_xp_levels");
            case "Spider" -> ApothitweaksJsonReader.config.contains("spiders_birth_cave_spiders_when_killed");
            case "Vex" -> ApothitweaksJsonReader.config.contains("vex_nerf");
            case "WaterDropParticle" -> ApothitweaksJsonReader.config.contains("transparent_rain_droplets");
            case "ItemFood" -> ApothitweaksJsonReader.config.contains("unstackable_food");
            case "Mob" -> ApothitweaksJsonReader.config.contains("block_light_mob_burning");
            default -> false;
        };
    }

    /**
     * Called after all configurations are initialised, this allows this plugin
     * to observe classes targetted by other mixin configs and optionally remove
     * targets from its own set. The set myTargets is a direct view of the
     * targets collection in this companion config and keys may be removed from
     * this set to suppress mixins in this config which target the specified
     * class. Adding keys to the set will have no effect.
     *
     * @param myTargets    Target class set from the companion config
     * @param otherTargets Target class set incorporating targets from all other
     *                     configs, read-only
     */
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    /**
     * After mixins specified in the configuration have been processed, this
     * method is called to allow the plugin to add any additional mixins to
     * load. It should return a list of mixin class names or return null if the
     * plugin does not wish to append any mixins of its own.
     *
     * @return additional mixins to apply
     */
    @Override
    public List<String> getMixins() {
        return null;
    }

    /**
     * Called immediately <b>before</b> a mixin is applied to a target class,
     * allows any pre-application transformations to be applied.
     *
     * @param targetClassName Transformed name of the target class
     * @param targetClass     Target class tree
     * @param mixinClassName  Name of the mixin class
     * @param mixinInfo       Information about this mixin
     */
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    /**
     * Called immediately <b>after</b> a mixin is applied to a target class,
     * allows any post-application transformations to be applied.
     *
     * @param targetClassName Transformed name of the target class
     * @param targetClass     Target class tree
     * @param mixinClassName  Name of the mixin class
     * @param mixinInfo       Information about this mixin
     */
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
