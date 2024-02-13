package com.Apothic0n.Apothitweaks.core.events;

import com.Apothic0n.Apothitweaks.Apothitweaks;
import com.Apothic0n.Apothitweaks.core.objects.PlayerPetProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.Apothic0n.Apothitweaks.core.ApothitweaksMath.getOffsetDouble;

@Mod.EventBusSubscriber(modid = Apothitweaks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    static void playerJoined(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.PLAYER) && !event.getLevel().isClientSide) {
            ServerPlayer player = (ServerPlayer) entity;
            player.awardRecipes(event.getLevel().getRecipeManager().getRecipes());
        }
    }

    @SubscribeEvent
    static void playerInteractEvent(PlayerInteractEvent event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        InteractionHand hand = player.getUsedItemHand();
        BlockPos pos = event.getPos();
        if (!level.getBlockState(pos).isSolid()) {
            List<Entity> entities = level.getEntities((Entity) null, new AABB(pos), EntitySelector.NO_SPECTATORS);
            for (Entity entity : entities) {
                if (entity.getType().equals(EntityType.CHICKEN)) {
                    player.swing(hand);
                    if (Math.random() * 33 <= 1) {
                        ItemStack itemStack = new ItemStack(Items.FEATHER);
                        ItemEntity itemEntity = new ItemEntity(level,
                                getOffsetDouble(pos.getX(), player.getEyePosition().x),
                                getOffsetDouble(pos.getY(), player.getEyePosition().y),
                                getOffsetDouble(pos.getZ(), player.getEyePosition().z),
                                itemStack);
                        level.addFreshEntity(itemEntity);
                    }
                    level.playSound(player, pos, SoundEvents.WOOL_STEP, SoundSource.NEUTRAL);
                    level.addParticle(ParticleTypes.SNOWFLAKE, false, entity.position().x, entity.position().y + 0.4, entity.position().z, Math.random() - 0.5, -0.5D, Math.random() - 0.5);
                }
            }
        }
    }

    @SubscribeEvent
    static void renderGuiOverlayEvent(RenderGuiOverlayEvent event) {
        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(Apothitweaks.MODID, "properties"), new PlayerPetProvider());
        }
    }

    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerPetProvider.PLAYER_PET).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerPetProvider.PLAYER_PET).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}
