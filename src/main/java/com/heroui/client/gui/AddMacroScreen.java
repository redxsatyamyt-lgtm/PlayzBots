package com.heroui.client.gui;

import com.heroui.client.HeroUIClient;
import com.heroui.config.MacroConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class AddMacroScreen extends Screen {

    private final Screen parent;
    private String category;
    private TextFieldWidget labelField;
    private TextFieldWidget commandField;
    private TextFieldWidget categoryField;

    public AddMacroScreen(Screen parent, String defaultCategory) {
        super(Text.literal("Add Macro"));
        this.parent = parent;
        this.category = defaultCategory == null ? "Custom" : defaultCategory;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 50;

        categoryField = new TextFieldWidget(this.textRenderer, centerX - 100, y, 200, 20, Text.literal("Category"));
        categoryField.setText(category);
        categoryField.setMaxLength(32);
        addDrawableChild(categoryField);
        y += 30;

        labelField = new TextFieldWidget(this.textRenderer, centerX - 100, y, 200, 20, Text.literal("Button label"));
        labelField.setMaxLength(40);
        labelField.setPlaceholder(Text.literal("e.g. Spawn 5 Bots"));
        addDrawableChild(labelField);
        y += 30;

        commandField = new TextFieldWidget(this.textRenderer, centerX - 100, y, 200, 20, Text.literal("Command"));
        commandField.setMaxLength(200);
        commandField.setPlaceholder(Text.literal("e.g. pvpbot bot-management mass-spawn 5"));
        addDrawableChild(commandField);
        y += 40;

        addDrawableChild(ButtonWidget.builder(Text.literal("Save"), b -> save())
                .dimensions(centerX - 100, y, 95, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), b -> client.setScreen(parent))
                .dimensions(centerX + 5, y, 95, 20).build());
    }

    private void save() {
        String cat = categoryField.getText().trim();
        String label = labelField.getText().trim();
        String cmd = commandField.getText().trim();
        if (cat.isEmpty() || label.isEmpty() || cmd.isEmpty()) return;

        MacroConfig cfg = HeroUIClient.CONFIG;
        cfg.categories.computeIfAbsent(cat, k -> new ArrayList<>())
                .add(new MacroConfig.Macro(label, cmd));
        cfg.save();
        client.setScreen(new ControlPanelScreen());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        context.drawText(this.textRenderer, "Category", this.width / 2 - 100, 40, 0xAAAAAA, false);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
