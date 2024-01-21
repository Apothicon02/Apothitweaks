package com.Apothic0n.Apothitweaks.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Inject(method = "handleRenameItem", at = @At("TAIL"))
    public void handleRenameItem(ServerboundRenameItemPacket packet, CallbackInfo ci) {
        ItemStack nameTag = player.getItemInHand(InteractionHand.OFF_HAND);
        if (nameTag.is(Items.NAME_TAG)) {
            nameTag.setHoverName(Component.literal(packet.getName()));
        }
    }
}
