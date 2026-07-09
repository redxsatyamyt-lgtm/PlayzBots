package com.heroui.client.gui;

import com.heroui.client.HeroUIClient;
import com.heroui.config.MacroConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControlPanelScreen extends Screen {

    private String selectedCategory = null;
    private final List<ButtonWidget> dynamicButtons = new ArrayList<>();

    public ControlPanelScreen() {
        super(Text.literal("HeroUI Control Panel"));
    }

    @Override
    protected void init() {
        MacroConfig cfg = HeroUIClient.CONFIG;
        if (selectedCategory == null && !cfg.categories.isEmpty()) {
            selectedCategory = cfg.categories.keySet().iterator().next();
        }

        int sidebarX = 10;
        int sidebarY = 30;
        int sidebarWidth = 110;

        int i = 0;
        for (String category : cfg.categories.keySet()) {
            int by = sidebarY + i * 22;
            ButtonWidget btn = ButtonWidget.builder(Text.literal(category), b -> {
                selectedCategory = category;
                clearAndInit();
            }).dimensions(sidebarX, by, sidebarWidth, 20).build();
            addDrawableChild(btn);
            i++;
        }

        int bottomY = sidebarY + i * 22 + 10;
        addDrawableChild(ButtonWidget.builder(Text.literal("+ Add Macro"), b ->
                client.setScreen(new AddMacroScreen(this, selectedCategory))
        ).dimensions(sidebarX, bottomY, sidebarWidth, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), b ->
                client.setScreen(new PanelSettingsScreen(this))
        ).dimensions(sidebarX, bottomY + 24, sidebarWidth, 20).build());

        int mainX = sidebarX + sidebarWidth + 16;
        int mainY = 30;
        int col = 0, row = 0;
        int btnWidth = 140, btnHeight = 20, gapX = 8, gapY = 6;
        int perRow = Math.max(1, (this.width - mainX - 10) / (btnWidth + gapX));

        if (selectedCategory != null && cfg.categories.containsKey(selectedCategory)) {
            for (MacroConfig.Macro macro : cfg.categories.get(selectedCategory)) {
                int bx = mainX + col * (btnWidth + gapX);
                int by = mainY + row * (btnHeight + gapY);
                ButtonWidget btn = ButtonWidget.builder(Text.literal(macro.label), b ->
                        HeroUIClient.sendCommand(macro.command)
                ).dimensions(bx, by, btnWidth, btnHeight).build();
                addDrawableChild(btn);

                col++;
                if (col >= perRow) {
                    col = 0;
                    row++;
                }
            }
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
                .dimensions(this.width - 70, 6, 60, 18).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        if (selectedCategory == null) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("No categories yet - click '+ Add Macro' to create one"),
                    this.width / 2, this.height / 2, 0xAAAAAA);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
