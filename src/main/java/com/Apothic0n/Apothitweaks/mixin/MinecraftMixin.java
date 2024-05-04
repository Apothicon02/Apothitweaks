package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow private int rightClickDelay;
    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Final public Options options;
    @Shadow @Nullable public HitResult hitResult;
    @Shadow @Nullable public ClientLevel level;
    @Unique private int apothitweaks$ticksUseDown = 0;
    @Unique private BlockHitResult apothitweaks$prevHit = null;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (this.options.keyUse.isDown()) {
            apothitweaks$ticksUseDown++;
        } else {
            apothitweaks$ticksUseDown = 0;
        }
        BlockHitResult hit = null;
        if (this.hitResult != null && this.hitResult.getType().equals(HitResult.Type.BLOCK)) {
            hit = (BlockHitResult) this.hitResult;
            if (apothitweaks$ticksUseDown > 6 && apothitweaks$prevHit != null && this.rightClickDelay <= 4) {
                this.rightClickDelay = 0;
            }
        }
        apothitweaks$prevHit = hit;
    }
}
