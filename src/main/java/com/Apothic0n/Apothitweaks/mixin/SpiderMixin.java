package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Spider.class)
public class SpiderMixin extends Mob {

    protected SpiderMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        if (!this.isAlive() && this.getType().equals(EntityType.SPIDER)) {
            for (int i = 0; i <= Math.random()*3; i++) {
                EntityType.CAVE_SPIDER.spawn((ServerLevel) this.level(), this.blockPosition(), MobSpawnType.TRIGGERED);
            }
        }
        this.setRemoved(reason);
        this.invalidateCaps();
    }
}
