# HeroUI

A lightweight companion mod for **HeroBot** + **PVP Bot** (Fabric, MC 1.21.11).
Adds a proper GUI control panel, a HUD, rebindable keybinds, and an
**unlimited custom command macro system** — instead of hardcoding 50 features
(which would be slow and buggy), you get a small, stable core plus the
ability to add as many buttons as you want, each one just running a
`/pvpbot ...` or `/herobot ...` command for you.

This mod does **not** copy any code from HeroBot or PVP Bot. It only sends
normal chat commands to them, so it stays legal and independent of their
license.

---

## ⚠️ You need to build this yourself (or let GitHub do it for you)

I can't compile a `.jar` for you directly — building a Fabric mod needs
internet access to download Minecraft, mappings, and Fabric API, which this
chat environment doesn't have. But since you said you're a beginner, here's
the **easiest path that needs zero local installs**:

### Option A — Let GitHub build it for you (recommended, no installs)
1. Create a free GitHub account if you don't have one.
2. Create a new **public** repository, e.g. `heroui-mod`.
3. Upload every file/folder from this project into that repo (drag-and-drop
   works fine on github.com — click "Add file" → "Upload files").
4. Go to the **Actions** tab of your repo. You should see a workflow called
   "Build HeroUI Mod" running automatically (it's triggered by the
   `.github/workflows/build.yml` file already included here).
5. Wait for it to finish (a few minutes). Click into the finished run →
   scroll down to **Artifacts** → download `heroui-jar`. That's your
   compiled `.jar`.
6. Drop it into your `.minecraft/mods` folder next to `herobot` and
   `pvp_bot`.

If the build fails, open the Actions log — it's almost always because
`gradle.properties` has an outdated version number (see below).

### Option B — Build locally (if you get comfortable with Java later)
1. Install **JDK 21** (Temurin/Adoptium build recommended).
2. Open a terminal in this project folder.
3. Run `gradle wrapper` once (needs Gradle installed, or use
   `sdk install gradle` via SDKMAN).
4. Run `./gradlew build` (Linux/Mac) or `gradlew.bat build` (Windows).
5. Your jar appears in `build/libs/`.

---

## 🔧 Before building: double-check versions

Fabric API updates constantly. Open `gradle.properties` and confirm the
numbers against **https://fabricmc.net/develop** (select Minecraft
**1.21.11**) — specifically `yarn_mappings` and `fabric_version`. If GitHub
Actions build fails with a "could not resolve" error, it's almost always
this.

---

## 🎮 How to use it once installed

- Press **G** in-game to open the HeroUI control panel (rebind anytime via
  Options → Controls → Key Binds → HeroUI category).
- Press **F9** to hide/show the HUD (useful when recording).
- Press **F10** to hide/show bot nametags (useful when recording).
- Click **"+ Add Macro"** in the panel to add your own button: give it a
  category, a label, and the exact command (without the leading `/`), e.g.
  `pvpbot faction attack attackers Steve`.
- All your macros are saved to `config/heroui/macros.json` — you can also
  edit that file directly with a text editor for bulk changes, or share it
  with friends.

### Built-in starter macros
The panel comes pre-loaded with categories: **Spawn**, **Combat**,
**Realism**, **Factions**, **Recording Helpers** — covering the most common
PVP Bot commands, so you're not starting from zero.

---

## 🗺️ Project structure

```
heroui-mod/
├── build.gradle              - build config
├── gradle.properties         - MC/Fabric version numbers (check these!)
├── settings.gradle
├── .github/workflows/build.yml - auto-build on GitHub
└── src/main/
    ├── resources/
    │   ├── fabric.mod.json
    │   ├── heroui.mixins.json
    │   └── assets/heroui/lang/en_us.json
    └── java/com/heroui/
        ├── client/HeroUIClient.java       - entrypoint, keybinds, HUD hook
        ├── client/gui/ControlPanelScreen.java  - main panel
        ├── client/gui/AddMacroScreen.java      - "add macro" form
        ├── client/gui/PanelSettingsScreen.java - toggles
        ├── client/hud/BotHudOverlay.java       - corner HUD
        ├── client/mixin/NameTagMixin.java      - optional nametag hider
        └── config/MacroConfig.java             - saves/loads your macros
```

---

## 🚀 Ideas for extending it later (once you're comfortable, or ask me again)

- Icon support for macro buttons (there's already an unused `icon` field
  in `MacroConfig.Macro` ready for it)
- A "battle preset" one-click button that chains multiple commands
  (create 2 factions, mass-spawn both, set hostile) — can be done today
  actually, just add a macro whose command uses Minecraft's `/execute` +
  a function file, or ask me to build a proper multi-command macro runner
- Drag-to-reorder categories
- A search bar to filter macros when you have a lot of them
