package com.mevchu.mixin;

import com.mevchu.MevchuClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void onLeftClick(CallbackInfo ci) {
        MevchuClient.cpsMod.onLeftClick();
    }

    @Inject(method = "doItemUse", at = @At("HEAD"))
    private void onRightClick(CallbackInfo ci) {
        MevchuClient.cpsMod.onRightClick();
    }
}
