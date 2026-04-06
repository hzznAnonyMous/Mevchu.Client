package com.mevchu;

import com.mevchu.config.MevchuConfig;
import com.mevchu.hud.HudRenderer;
import com.mevchu.mods.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MevchuClient implements ClientModInitializer {

    public static final String MOD_ID   = "mevchu";
    public static final String MOD_NAME = "Mevchu Client";
    public static final Logger LOGGER   = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding openGuiKey;
    public static MevchuClient INSTANCE;
    public static MevchuConfig    config;
    public static ToggleSprintMod toggleSprint;
    public static CpsMod          cpsMod;
    public static HudRenderer     hudRenderer;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER.info("[{}] Khoi dong...", MOD_NAME);

        config = new MevchuConfig();
        config.load();

        toggleSprint = new ToggleSprintMod();
        cpsMod       = new CpsMod();
        hudRenderer  = new HudRenderer();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.mevchu.opengui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.mevchu"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            toggleSprint.onTick(client);
            cpsMod.onTick();
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new com.mevchu.gui.MevchuGuiScreen());
                }
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) ->
            hudRenderer.render(drawContext, tickDelta)
        );

        LOGGER.info("[{}] San sang! Nhan [R] de mo menu.", MOD_NAME);
    }
          }
