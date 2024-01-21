package com.Apothic0n.Apothitweaks.core.objects;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenameTagScreen extends Screen {
    private EditBox text;
    private TextFieldHelper textHelper;

    public RenameTagScreen(ItemStack itemStack) {
        super(Component.translatable("gui.apothitweaks.rename_button"));
    }

    private String getText() {
        return getTextBox().getValue();
    }

    private void setText(String string) {
        getTextBox().setValue(string);
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
        super.init();
        textHelper = new TextFieldHelper(this::getText, this::setText, this::getClipboard, this::setClipboard, (stringPredicate) -> {
            return stringPredicate.length() < 20 && this.font.wordWrapHeight(stringPredicate, 114) < 128;
        });

        this.addRenderableWidget(getTextBox());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.apothitweaks.rename_button"), (thing) -> {
            onNameChanged(text.getValue());
        }).bounds(this.width / 2 + 2, 196, 98, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (p_98157_) -> {
            this.minecraft.setScreen((Screen) null);
        }).bounds(this.width / 2 - 100, 196, 98, 20).build());
    }

    protected EditBox getTextBox() {
        if (this.text == null) {
            EditBox tempEdit = new EditBox(this.font, this.width / 2 -99, 172, 196, 20, Component.translatable("container.repair"));
            tempEdit.setMaxLength(20);
            tempEdit.setHint(Component.translatable("gui.apothitweaks.rename_hint"));
            tempEdit.setVisible(true);
            this.text = tempEdit;
        }
        return this.text;
    }

    @Override
    public void resize(Minecraft mc, int w, int h) {
        this.width = w;
        this.height = h;
        this.repositionElements();
        this.text.setX(w / 2 -99);
    }

    private void onNameChanged(String str) {
        this.minecraft.player.connection.send(new ServerboundRenameItemPacket(str));
        this.minecraft.setScreen((Screen) null);
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