package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.WaterDropParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WaterDropParticle.class)
public class WaterDropParticleMixin {
    /**
     * @author Apothicon
     * @reason Makes water droplets transparent.
     */
    @Overwrite
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
