package com.mevchu.config;

import com.google.gson.*;
import com.mevchu.MevchuClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;

public class MevchuConfig {

    private static final Path CONFIG_PATH =
        FabricLoader.getInstance().getConfigDir().resolve("mevchu.json");

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean toggleSprintEnabled = true;
    public boolean fpsEnabled   = true;
    public int     fpsX         = 2;
    public int     fpsY         = 2;
    public int     fpsColor     = 0xFFFFFF00;
    public boolean cpsEnabled   = true;
    public int     cpsX         = 2;
    public int     cpsY         = 14;
    public int     cpsColor     = 0xFF00FFFF;
    public boolean keystrokesEnabled = true;
    public int     keystrokesX       = 2;
    public int     keystrokesY       = 26;
    public boolean coordsEnabled = true;
    public int     coordsX       = 2;
    public int     coordsY       = 80;
    public int     coordsColor   = 0xFFAAFFAA;
    public boolean armorEnabled = true;
    public int     armorX       = 2;
    public int     armorY       = 110;
    public boolean potionEnabled = true;
    public int     potionX       = 2;
    public int     potionY       = 150;
    public boolean pingEnabled  = true;
    public int     pingX        = 2;
    public int     pingY        = 38;
    public int     pingColor    = 0xFF55FF55;
    public boolean hideScoreboard = false;
    public boolean sprintIndicator = true;
    public int     sprintX         = 2;
    public int     sprintY         = 50;

    public void load() {
        if (!Files.exists(CONFIG_PATH)) { save(); return; }
        try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
            JsonObject obj = JsonParser.parseReader(r).getAsJsonObject();
            toggleSprintEnabled = getBool(obj, "toggleSprintEnabled", toggleSprintEnabled);
            fpsEnabled          = getBool(obj, "fpsEnabled",          fpsEnabled);
            fpsX                = getInt (obj, "fpsX",                fpsX);
            fpsY                = getInt (obj, "fpsY",                fpsY);
            fpsColor            = getInt (obj, "fpsColor",            fpsColor);
            cpsEnabled          = getBool(obj, "cpsEnabled",          cpsEnabled);
            cpsX                = getInt (obj, "cpsX",                cpsX);
            cpsY                = getInt (obj, "cpsY",                cpsY);
            cpsColor            = getInt (obj, "cpsColor",            cpsColor);
            keystrokesEnabled   = getBool(obj, "keystrokesEnabled",   keystrokesEnabled);
            keystrokesX         = getInt (obj, "keystrokesX",         keystrokesX);
            keystrokesY         = getInt (obj, "keystrokesY",         keystrokesY);
            coordsEnabled       = getBool(obj, "coordsEnabled",       coordsEnabled);
            coordsX             = getInt (obj, "coordsX",             coordsX);
            coordsY             = getInt (obj, "coordsY",             coordsY);
            coordsColor         = getInt (obj, "coordsColor",         coordsColor);
            armorEnabled        = getBool(obj, "armorEnabled",        armorEnabled);
            armorX              = getInt (obj, "armorX",              armorX);
            armorY              = getInt (obj, "armorY",              armorY);
            potionEnabled       = getBool(obj, "potionEnabled",       potionEnabled);
            potionX             = getInt (obj, "potionX",             potionX);
            potionY             = getInt (obj, "potionY",             potionY);
            pingEnabled         = getBool(obj, "pingEnabled",         pingEnabled);
            pingX               = getInt (obj, "pingX",               pingX);
            pingY               = getInt (obj, "pingY",               pingY);
            pingColor           = getInt (obj, "pingColor",           pingColor);
            hideScoreboard      = getBool(obj, "hideScoreboard",      hideScoreboard);
            sprintIndicator     = getBool(obj, "sprintIndicator",     sprintIndicator);
            sprintX             = getInt (obj, "sprintX",             sprintX);
            sprintY             = getInt (obj, "sprintY",             sprintY);
        } catch (Exception e) {
            MevchuClient.LOGGER.warn("[Mevchu] Loi load config: {}", e.getMessage());
        }
    }

    public void save() {
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("toggleSprintEnabled", toggleSprintEnabled);
            obj.addProperty("fpsEnabled",          fpsEnabled);
            obj.addProperty("fpsX",                fpsX);
            obj.addProperty("fpsY",                fpsY);
            obj.addProperty("fpsColor",            fpsColor);
            obj.addProperty("cpsEnabled",          cpsEnabled);
            obj.addProperty("cpsX",                cpsX);
            obj.addProperty("cpsY",                cpsY);
            obj.addProperty("cpsColor",            cpsColor);
            obj.addProperty("keystrokesEnabled",   keystrokesEnabled);
            obj.addProperty("keystrokesX",         keystrokesX);
            obj.addProperty("keystrokesY",         keystrokesY);
            obj.addProperty("coordsEnabled",       coordsEnabled);
            obj.addProperty("coordsX",             coordsX);
            obj.addProperty("coordsY",             coordsY);
            obj.addProperty("coordsColor",         coordsColor);
            obj.addProperty("armorEnabled",        armorEnabled);
            obj.addProperty("armorX",              armorX);
            obj.addProperty("armorY",              armorY);
            obj.addProperty("potionEnabled",       potionEnabled);
            obj.addProperty("potionX",             potionX);
            obj.addProperty("potionY",             potionY);
            obj.addProperty("pingEnabled",         pingEnabled);
            obj.addProperty("pingX",               pingX);
            obj.addProperty("pingY",               pingY);
            obj.addProperty("pingColor",           pingColor);
            obj.addProperty("hideScoreboard",      hideScoreboard);
            obj.addProperty("sprintIndicator",     sprintIndicator);
            obj.addProperty("sprintX",             sprintX);
            obj.addProperty("sprintY",             sprintY);
            Files.writeString(CONFIG_PATH, gson.toJson(obj));
        } catch (Exception e) {
            MevchuClient.LOGGER.warn("[Mevchu] Loi save config: {}", e.getMessage());
        }
    }

    private boolean getBool(JsonObject o, String k, boolean def) {
        return o.has(k) ? o.get(k).getAsBoolean() : def;
    }
    private int getInt(JsonObject o, String k, int def) {
        return o.has(k) ? o.get(k).getAsInt() : def;
    }
}
