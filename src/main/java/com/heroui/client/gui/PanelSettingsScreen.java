package com.heroui.client.gui;

import com.heroui.client.HeroUIClient;
import com.heroui.config.MacroConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class PanelSettingsScreen extends Screen {

    private final Screen parent;

    public PanelSettingsScreen(Screen parent) {
        super(Text.literal("HeroUI Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        MacroConfig cfg = HeroUIClient.CONFIG;
        int centerX = this.width / 2;
        int y = 50;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("HUD counter: " + (cfg.showHudCounter ? "ON" : "OFF")),
                b -> {
                    cfg.showHudCounter = !cfg.showHudCounter;
                    cfg.save();
                    client.setScreen(new PanelSettingsScreen(parent));
                }).dimensions(centerX - 100, y, 200, 20).build());
        y += 26;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Hide bot nametags: " + (cfg.hideBotNameTags ? "ON" : "OFF")),
                b -> {
                    cfg.hideBotNameTags = !cfg.hideBotNameTags;
                    cfg.save();
                    client.setScreen(new PanelSettingsScreen(parent));
                }).dimensions(centerX - 100, y, 200, 20).build());
        y += 26;

        addDrawableChild(ButtonWidget.builder(Text.literal("Rebind keys (opens Controls menu)"), b -> {
            client.setScreen(new ControlsOptionsScreen(this, client.options));
        }).dimensions(centerX - 100, y, 200, 20).build());
        y += 40;

        addDrawableChild(ButtonWidget.builder(Text.literal("Back"), b -> client.setScreen(parent))
                .dimensions(centerX - 100, y, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
