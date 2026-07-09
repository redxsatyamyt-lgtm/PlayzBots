package com.heroui.client.hud;

import com.heroui.client.HeroUIClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class BotHudOverlay {

    public static void render(DrawContext context) {
        if (!HeroUIClient.CONFIG.showHudCounter) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        int x = 6;
        int y = 6;
        int color = 0xFFFFFF;
        int bg = 0x66000000;

        String line1 = "HeroUI ready";
        String line2 = "Press panel key to open";

        context.fill(x - 3, y - 2, x + 150, y + 24, bg);
        context.drawText(client.textRenderer, line1, x, y, color, true);
        context.drawText(client.textRenderer, line2, x, y + 11, 0xAAAAAA, true);
    }
}
