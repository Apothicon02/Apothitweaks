package com.Apothic0n.Apothitweaks.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ApothitweaksJsonReader {
    public static List<String> config;
    public static void readConfig(Path path) throws IOException {
        Gson gson = new Gson();
        if (!Files.exists(path)) {
            JsonWriter writer = new JsonWriter(new FileWriter(path.toString()));
            JsonObject defaultData = gson.fromJson("{\"Put a . at the end an entry to disable it\":[\"further_block_reach\",\"instant_block_placement\",\"stackable_potions\",\"cartographer_sells_recovery_compass\",\"unlock_recipe_book\",\"pet_chickens_for_feathers\",\"no_trampling\",\"mounts\",\"custom_fog\",\"paths\",\"noodle_caves\",\"underground_rivers\",\"minecart_camera_fix\",\"faster_minecarts\",\"inaccurate_skeletons\",\"anvil_overhaul\",\"faster_boats\",\"pitcher_plant_fills_bottles\",\"slower_day_night_cycle\",\"creepers_explode_into_fire\",\"only_unbreaking_on_loot\",\"limited_librarian_enchants\",\"enchantment_table_overhaul\",\"louder_hostile_footsteps\",\"no_elytra_boosting\",\"hunger_and_healing_overhaul\",\"food_on_stick_auto_recrafting\",\"sticks_convert_to_torches_on_fires_and_bonemealable_flowers\",\"longer_despawn_timer\",\"faster_high_end_tools\",\"poison_duration_nerf\",\"name_tag_overhaul\",\"faster_pig_riding\",\"no_death_item_scattering\",\"limited_sprinting\",\"flat_xp_levels\",\"spiders_birth_cave_spiders_when_killed\",\"vex_nerf\",\"transparent_rain_droplets\"]}", JsonObject.class);
            gson.toJson(defaultData, writer);
            writer.close();
        }
        JsonReader reader = new JsonReader(new FileReader(path.toString()));
        JsonObject data = gson.fromJson(reader, JsonObject.class);
        JsonArray configNames = data.get("Put a . at the end an entry to disable it").getAsJsonArray();
        List<String> tempConfig = new ArrayList<>(List.of());
        for (int i = 0; i < configNames.size(); i++) {
            tempConfig.add(configNames.get(i).getAsString());
        }
        config = tempConfig;
    }

}