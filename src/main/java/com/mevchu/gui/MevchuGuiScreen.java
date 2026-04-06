package com.mevchu.gui;

import com.mevchu.MevchuClient;
import com.mevchu.config.MevchuConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MevchuGuiScreen extends Screen {

    private static final int BTN_W = 150;
    private static final int BTN_H = 20;
    private static final int GAP   = 4;

    public MevchuGuiScreen() {
        super(Text.literal("Mevchu Client"));
    }

    @Override
    protected void init() {
        MevchuConfig cfg = MevchuClient.config;
        int centerX = this.width / 2;
        int startY  = 50;
        int col1    = centerX - BTN_W - GAP / 2;
        int col2    = centerX + GAP / 2;

        addToggle(col1, startY,      "FPS Display",      cfg.fpsEnabled,          v -> { cfg.fpsEnabled          = v; save(); });
        addToggle(col1, startY + 24, "CPS Counter",      cfg.cpsEnabled,          v -> { cfg.cpsEnabled          = v; save(); });
        addToggle(col1, startY + 48, "Keystrokes",       cfg.keystrokesEnabled,   v -> { cfg.keystrokesEnabled   = v; save(); });
        addToggle(col1, startY + 72, "Coordinates",      cfg.coordsEnabled,       v -> { cfg.coordsEnabled       = v; save(); });
        addToggle(col1, startY + 96, "Sprint Indicator", cfg.sprintIndicator,     v -> { cfg.sprintIndicator     = v; save(); });

        addToggle(col2, startY,      "ToggleSprint",     cfg.toggleSprintEnabled, v -> { cfg.toggleSprintEnabled = v; save(); });
        addToggle(col2, startY + 24, "Armor Status",     cfg.armorEnabled,        v -> { cfg.armorEnabled        = v; save(); });
        addToggle(col2, startY + 48, "Potion Status",    cfg.potionEnabled,       v -> { cfg.potionEnabled       = v; save(); });
        addToggle(col2, startY + 72, "Ping Display",     cfg.pingEnabled,         v -> { cfg.pingEnabled         = v; save(); });
        addToggle(col2, startY + 96, "Hide Scoreboard",  cfg.hideScoreboard,      v -> { cfg.hideScoreboard      = v; save(); });

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Dong"),
            btn -> this.close()
        ).dimensions(centerX - 50, startY + 130, 100, BTN_H).build());
    }

    private void addToggle(int x, int y, String label, boolean current,
                           java.util.function.Consumer<Boolean> onChange) {
        boolean[] state = {current};
        ButtonWidget btn = ButtonWidget.builder(
            buildLabel(label, state[0]),
            b -> {
                state[0] = !state[0];
                onChange.accept(state[0]);
                b.setMessage(buildLabel(label, state[0]));
            }
        ).dimensions(x, y, BTN_W, BTN_H).build();
        this.addDrawableChild(btn);
    }

    private Text buildLabel(String label, boolean on) {
        return Text.literal((on ? "[ON] " : "[OFF] ") + label);
    }

    private void save() {
        MevchuClient.config.save();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        int cx = this.width / 2;
        ctx.drawCenteredTextWithShadow(this.textRenderer, "Mevchu Client v1.0", cx, 20, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(this.textRenderer, "Nhan [R] de dong", cx, 35, 0xAAAAAA);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
          }
