package net.spartanb312.phantom.mixin;

import net.minecraft.client.Minecraft;
import net.spartanb312.phantom.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "init", at = @At("HEAD"))
    private void onPreInit(CallbackInfo ci) {
        Phantom.INSTANCE.onPreInit();
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onInit(CallbackInfo ci) {
        Phantom.INSTANCE.onInit();
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void onPostInit(CallbackInfo ci) {
        Phantom.INSTANCE.onPostInit();
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onReady(CallbackInfo ci) {
        Phantom.INSTANCE.onReady();
    }

}