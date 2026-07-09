package com.heroui.client.mixin;

import com.heroui.client.HeroUIClient;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class NameTagMixin {

    @Inject(method = "hasLabel", at = @At("HEAD"), cancellable = true, remap = true, require = 0)
    private void heroui$hideNameTag(EntityRenderState state, CallbackInfoReturnable<Boolean> cir) {
        if (HeroUIClient.CONFIG != null && HeroUIClient.CONFIG.hideBotNameTags) {
            cir.setReturnValue(false);
        }
    }
}
