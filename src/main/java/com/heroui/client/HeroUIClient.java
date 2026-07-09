package com.heroui.client;

import com.heroui.client.gui.ControlPanelScreen;
import com.heroui.client.hud.BotHudOverlay;
import com.heroui.config.MacroConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class HeroUIClient implements ClientModInitializer {

    public static final String MOD_ID = "heroui";

    // Public so the GUI/HUD classes can read+save it too
    public static MacroConfig CONFIG;

    // Toggle state for recording helpers
    public static boolean hudHidden = false;

    private static KeyBinding openPanelKey;
    private static KeyBinding toggleHudKey;
    private static KeyBinding toggleNameTagsKey;

    @Override
    public void onInitializeClient() {
        CONFIG = MacroConfig.loadOrCreateDefault();

        // Default keybind: G opens the control panel. Fully rebindable
        // in-game via Options -> Controls -> Key Binds -> HeroUI category.
        openPanelKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.heroui.open_panel",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.heroui"
        ));

        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.heroui.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F9,
                "category.heroui"
        ));

        toggleNameTagsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.heroui.toggle_nametags",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F10,
                "category.heroui"
        ));

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (!hudHidden) {
                BotHudOverlay.render(drawContext);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openPanelKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ControlPanelScreen());
                }
            }
            while (toggleHudKey.wasPressed()) {
                hudHidden = !hudHidden;
            }
            while (toggleNameTagsKey.wasPressed()) {
                CONFIG.hideBotNameTags = !CONFIG.hideBotNameTags;
                CONFIG.save();
            }
        });
    }

    /** Sends a command to the server exactly like typing it in chat, minus the leading slash. */
    public static void sendCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        String cmd = command.startsWith("/") ? command.substring(1) : command;
        client.player.networkHandler.sendChatCommand(cmd);
    }
}
