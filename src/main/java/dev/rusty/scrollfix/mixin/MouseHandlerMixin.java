package dev.rusty.scrollfix.mixin;

import dev.rusty.scrollfix.ScrollDebounce;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void scrollfixWayland$dropDuplicate(long window, double xOffset, double yOffset, CallbackInfo ci) {
        if (ScrollDebounce.shouldDrop(yOffset)) ci.cancel();
    }
}
