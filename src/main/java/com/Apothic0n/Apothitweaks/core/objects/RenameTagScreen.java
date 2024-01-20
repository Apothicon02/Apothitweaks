package com.Apothic0n.Apothitweaks.core.objects;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class RenameTagScreen extends Screen {
    private ItemStack nameTag;
    private String text = "";
    private final TextFieldHelper textHelper = new TextFieldHelper(this::getText, this::setText, this::getClipboard, this::setClipboard, (stringPredicate) -> {
        return stringPredicate.length() < 20 && this.font.wordWrapHeight(stringPredicate, 114) < 128;
    });

    public RenameTagScreen(ItemStack itemStack) {
        super(Component.translatable("gui.apothitweaks.rename_tag_title"));
        nameTag = itemStack;
        text = itemStack.getHoverName().getString();
    }

    private String getText() {
        return text;
    }

    private void setText(String string) {
        text = string;
    }

    private String getClipboard() {
        return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
    }

    private void setClipboard(String string) {
        if (this.minecraft != null) {
            TextFieldHelper.setClipboardContents(this.minecraft, string);
        }
    }

    protected void init() {
        this.addRenderableWidget(Button.builder(Component.translatable("gui.apothitweaks.rename"), (thing) -> {
            nameTag.setHoverName(Component.literal(text));
            this.minecraft.setScreen((Screen) null);
        }).bounds(this.width / 2 - 100, 196, 98, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (p_98157_) -> {
            this.minecraft.setScreen((Screen) null);
        }).bounds(this.width / 2 + 2, 196, 98, 20).build());
    }

    public boolean charTyped(char character, int number) {
        if (super.charTyped(character, number)) {
            return true;
        } else if (SharedConstants.isAllowedChatCharacter(character)) {
            this.textHelper.insertText(Character.toString(character));
            return true;
        } else {
            return false;
        }
    }
}