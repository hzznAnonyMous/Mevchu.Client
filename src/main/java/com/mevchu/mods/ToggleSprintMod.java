package com.mevchu.mods;

import com.mevchu.MevchuClient;
import net.minecraft.client.MinecraftClient;

public class ToggleSprintMod {

    private boolean sprinting = false;

    public void onTick(MinecraftClient client) {
        if (!MevchuClient.config.toggleSprintEnabled) return;
        if (client.player == null) return;

        if (client.options.forwardKey.isPressed()
                && !client.player.isSneaking()
                && !client.player.isSwimming()
                && client.player.isOnGround()) {
            client.player.setSprinting(true);
            sprinting = true;
        }

        if (!client.options.forwardKey.isPressed() || client.player.isSneaking()) {
            sprinting = false;
        }
    }

    public boolean isSprinting() { return sprinting; }
}
