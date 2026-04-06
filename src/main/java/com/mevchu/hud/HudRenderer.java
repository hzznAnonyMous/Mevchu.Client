package com.mevchu.hud;

import com.mevchu.MevchuClient;
import com.mevchu.config.MevchuConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import java.util.Collection;

public class HudRenderer {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public void render(DrawContext ctx, float tickDelta) {
        if (mc.player == null || mc.world == null) return;
        if (mc.options.hudHidden) return;
        MevchuConfig cfg = MevchuClient.config;
        ClientPlayerEntity player = mc.player;
        if (cfg.fpsEnabled)        renderFps(ctx, cfg);
        if (cfg.cpsEnabled)        renderCps(ctx, cfg);
        if (cfg.keystrokesEnabled) renderKeystrokes(ctx, cfg);
        if (cfg.coordsEnabled)     renderCoords(ctx, cfg, player);
        if (cfg.armorEnabled)      renderArmor(ctx, cfg, player);
        if (cfg.potionEnabled)     renderPotions(ctx, cfg, player);
        if (cfg.pingEnabled)       renderPing(ctx, cfg);
        if (cfg.sprintIndicator)   renderSprintIndicator(ctx, cfg);
    }

    private void renderFps(DrawContext ctx, MevchuConfig cfg) {
        drawTextWithShadow(ctx, "FPS: " + MinecraftClient.getCurrentFps(), cfg.fpsX, cfg.fpsY, cfg.fpsColor);
    }

    private void renderCps(DrawContext ctx, MevchuConfig cfg) {
        drawTextWithShadow(ctx, "CPS: " + MevchuClient.cpsMod.getLeftCps() + " | " + MevchuClient.cpsMod.getRightCps(), cfg.cpsX, cfg.cpsY, cfg.cpsColor);
    }

    private void renderKeystrokes(DrawContext ctx, MevchuConfig cfg) {
        int x = cfg.keystrokesX, y = cfg.keystrokesY, keyW = 14, keyH = 14, gap = 2;
        drawKey(ctx, x + keyW + gap, y, keyW, keyH, "W", mc.options.forwardKey.isPressed());
        drawKey(ctx, x, y + keyH + gap, keyW, keyH, "A", mc.options.leftKey.isPressed());
        drawKey(ctx, x + keyW + gap, y + keyH + gap, keyW, keyH, "S", mc.options.backKey.isPressed());
        drawKey(ctx, x + (keyW + gap) * 2, y + keyH + gap, keyW, keyH, "D", mc.options.rightKey.isPressed());
        drawKey(ctx, x, y + (keyH + gap) * 2, keyW * 3 + gap * 2, keyH, "SPC", mc.options.jumpKey.isPressed());
        drawKey(ctx, x, y + (keyH + gap) * 3, keyW + gap, keyH, "L", mc.options.attackKey.isPressed());
        drawKey(ctx, x + keyW + gap * 2, y + (keyH + gap) * 3, keyW + gap, keyH, "R", mc.options.useKey.isPressed());
    }

    private void drawKey(DrawContext ctx, int x, int y, int w, int h, String label, boolean pressed) {
        int bg = pressed ? 0xCC55FF55 : 0x99111111;
        int border = pressed ? 0xFF00FF00 : 0x66FFFFFF;
        int textC = pressed ? 0xFF000000 : 0xFFCCCCCC;
        ctx.fill(x, y, x + w, y + h, bg);
        ctx.fill(x, y, x + w, y + 1, border);
        ctx.fill(x, y + h - 1, x + w, y + h, border);
        ctx.fill(x, y, x + 1, y + h, border);
        ctx.fill(x + w - 1, y, x + w, y + h, border);
        ctx.drawText(mc.textRenderer, label, x + (w - mc.textRenderer.getWidth(label)) / 2, y + (h - 8) / 2, textC, false);
    }

    private void renderCoords(DrawContext ctx, MevchuConfig cfg, ClientPlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        drawTextWithShadow(ctx, String.format("X: %d  Y: %d  Z: %d", pos.getX(), pos.getY(), pos.getZ()), cfg.coordsX, cfg.coordsY, cfg.coordsColor);
        drawTextWithShadow(ctx, "Dir: " + getDirection(player.getYaw()), cfg.coordsX, cfg.coordsY + 10, cfg.coordsColor);
    }

    private String getDirection(float yaw) {
        yaw = ((yaw % 360) + 360) % 360;
        if (yaw < 22.5 || yaw >= 337.5) return "South (+Z)";
        if (yaw < 67.5) return "SW";
        if (yaw < 112.5) return "West (-X)";
        if (yaw < 157.5) return "NW";
        if (yaw < 202.5) return "North (-Z)";
        if (yaw < 247.5) return "NE";
        if (yaw < 292.5) return "East (+X)";
        return "SE";
    }

    private void renderArmor(DrawContext ctx, MevchuConfig cfg, ClientPlayerEntity player) {
        int x = cfg.armorX, y = cfg.armorY;
        drawTextWithShadow(ctx, "Armor:", x, y, 0xFFAAAAAA);
        String[] slots = {"Helmet", "Chest", "Legs", "Boots"};
        for (int i = 0; i < 4; i++) {
            ItemStack stack = player.getInventory().getArmorStack(3 - i);
            if (!stack.isEmpty()) {
                int pct = stack.getMaxDamage() > 0 ? ((stack.getMaxDamage() - stack.getDamage()) * 100 / stack.getMaxDamage()) : 100;
                int col = pct > 50 ? 0xFF55FF55 : pct > 20 ? 0xFFFFAA00 : 0xFFFF5555;
                String name = stack.getName().getString();
                if (name.length() > 12) name = name.substring(0, 12);
                drawTextWithShadow(ctx, name + " (" + pct + "%)", x + 4, y + 10 + i * 10, col);
            } else {
                drawTextWithShadow(ctx, slots[i] + ": None", x + 4, y + 10 + i * 10, 0xFF555555);
            }
        }
    }

    private void renderPotions(DrawContext ctx, MevchuConfig cfg, ClientPlayerEntity player) {
        Collection<StatusEffectInstance> effects = player.getStatusEffects();
        if (effects.isEmpty()) return;
        int x = cfg.potionX, y = cfg.potionY, i = 0;
        drawTextWithShadow(ctx, "Effects:", x, y, 0xFFAAAAAA);
        for (StatusEffectInstance effect : effects) {
            if (i >= 6) break;
            RegistryEntry<StatusEffect> type = effect.getEffectType();
            String name = type.value().getName().getString();
            int amp = effect.getAmplifier() + 1;
            int durSec = effect.getDuration() / 20;
            String time = durSec >= 60 ? (durSec / 60) + "m" + (durSec % 60) + "s" : durSec + "s";
            int col = !type.value().isBeneficial() ? 0xFFFF5555 : 0xFF55FFFF;
            drawTextWithShadow(ctx, name + (amp > 1 ? " " + amp : "") + " " + time, x + 4, y + 10 + i * 10, col);
            i++;
        }
    }

    private void renderPing(DrawContext ctx, MevchuConfig cfg) {
        if (mc.getNetworkHandler() == null || mc.player == null) return;
        var entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        if (entry == null) return;
        int ping = entry.getLatency();
        int col = ping < 80 ? 0xFF55FF55 : ping < 150 ? 0xFFFFFF55 : 0xFFFF5555;
        drawTextWithShadow(ctx, "Ping: " + ping + "ms", cfg.pingX, cfg.pingY, col);
    }

    private void renderSprintIndicator(DrawContext ctx, MevchuConfig cfg) {
        boolean s = MevchuClient.toggleSprint.isSprinting();
        drawTextWithShadow(ctx, s ? "SPRINTING" : "walking", cfg.sprintX, cfg.sprintY, s ? 0xFF00FF00 : 0xFF666666);
    }

    private void drawTextWithShadow(DrawContext ctx, String text, int x, int y, int color) {
        ctx.drawText(mc.textRenderer, text, x, y, color, true);
    }
}
