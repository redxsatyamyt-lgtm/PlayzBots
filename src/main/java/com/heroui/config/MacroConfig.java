package com.heroui.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MacroConfig {

    public static class Macro {
        public String label;
        public String command;
        public String icon;

        public Macro(String label, String command) {
            this.label = label;
            this.command = command;
        }
    }

    public Map<String, List<Macro>> categories = new LinkedHashMap<>();

    public boolean showHudCounter = true;
    public boolean hideBotNameTags = false;
    public String botNamePrefixFilter = "";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("heroui").resolve("macros.json");
    }

    public static MacroConfig loadOrCreateDefault() {
        Path path = configPath();
        try {
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    Type type = new TypeToken<MacroConfig>() {}.getType();
                    MacroConfig loaded = GSON.fromJson(reader, type);
                    if (loaded != null) {
                        return loaded;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MacroConfig fresh = buildDefaults();
        fresh.save();
        return fresh;
    }

    public void save() {
        Path path = configPath();
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MacroConfig buildDefaults() {
        MacroConfig cfg = new MacroConfig();

        List<Macro> spawn = new ArrayList<>();
        spawn.add(new Macro("Spawn Bot", "pvpbot spawn"));
        spawn.add(new Macro("Mass Spawn x10", "pvpbot bot-management mass-spawn 10"));
        spawn.add(new Macro("Mass Spawn x25", "pvpbot bot-management mass-spawn 25"));
        spawn.add(new Macro("Remove All", "pvpbot removeall"));
        spawn.add(new Macro("List Bots", "pvpbot bot-management list"));
        cfg.categories.put("Spawn", spawn);

        List<Macro> combat = new ArrayList<>();
        combat.add(new Macro("Combat ON", "pvpbot settings combat true"));
        combat.add(new Macro("Combat OFF", "pvpbot settings combat false"));
        combat.add(new Macro("Criticals ON", "pvpbot settings criticals true"));
        combat.add(new Macro("Ranged ON", "pvpbot settings ranged true"));
        combat.add(new Macro("Mace ON", "pvpbot settings mace true"));
        combat.add(new Macro("Auto-Armor ON", "pvpbot settings auto-armor true"));
        combat.add(new Macro("Auto-Weapon ON", "pvpbot settings auto-weapon true"));
        combat.add(new Macro("Auto-Shield ON", "pvpbot settings auto-shield true"));
        combat.add(new Macro("Auto-Totem ON", "pvpbot settings auto-totem true"));
        combat.add(new Macro("Retreat ON", "pvpbot settings retreat true"));
        cfg.categories.put("Combat", combat);

        List<Macro> realism = new ArrayList<>();
        realism.add(new Macro("Human-like Preset", "pvpbot settings miss-chance 15"));
        realism.add(new Macro("Mistake 10%", "pvpbot settings mistake-chance 10"));
        realism.add(new Macro("Aim Speed 15", "pvpbot settings aim-speed 15"));
        realism.add(new Macro("Bhop ON", "pvpbot settings bhop true"));
        cfg.categories.put("Realism", realism);

        List<Macro> factions = new ArrayList<>();
        factions.add(new Macro("Create 'attackers'", "pvpbot faction create attackers"));
        factions.add(new Macro("Create 'defenders'", "pvpbot faction create defenders"));
        factions.add(new Macro("Add-All -> attackers", "pvpbot faction add-all attackers"));
        factions.add(new Macro("Hostile A vs D", "pvpbot faction hostile attackers defenders true"));
        factions.add(new Macro("List Factions", "pvpbot faction list"));
        cfg.categories.put("Factions", factions);

        List<Macro> recording = new ArrayList<>();
        recording.add(new Macro("Reload Bot Configs", "pvpbot reload"));
        recording.add(new Macro("Show Bot Inventory", "pvpbot bot-management inventory"));
        cfg.categories.put("Recording Helpers", recording);

        return cfg;
    }
}
