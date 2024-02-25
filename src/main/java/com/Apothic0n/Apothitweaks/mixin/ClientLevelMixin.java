package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Supplier;

@Mixin(value = ClientLevel.class, priority = 1)
public abstract class ClientLevelMixin extends Level {
    protected ClientLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Shadow public abstract void setGameTime(long p_104638_);

    @Shadow public abstract void setDayTime(long p_104747_);

    @Unique
    public int apothitweaks$timeCooldown = 2;

    /**
     * @author Apothicon
     * @reason Make day/night cycle 3x slower.
     */
    @Overwrite
    private void tickTime() {
        this.setGameTime(this.levelData.getGameTime() + 1L);
        if (apothitweaks$timeCooldown <= 0) {
            apothitweaks$timeCooldown = 2;
            if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                this.setDayTime(this.levelData.getDayTime() + 1L);
            }
        } else {
            apothitweaks$timeCooldown = apothitweaks$timeCooldown - 1;
        }
    }
}
