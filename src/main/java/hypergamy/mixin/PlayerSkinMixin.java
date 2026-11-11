package hypergamy.mixin;

import hypergamy.HypergamyClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class PlayerSkinMixin {
    private static final Identifier HYPERGAMY_SKIN = Identifier.of("hypergamy", "textures/skin.png");

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void hypergamy$overrideSkin(CallbackInfoReturnable<SkinTextures> cir) {
        if (!HypergamyClient.enabled) return;
        SkinTextures original = cir.getReturnValue();
        cir.setReturnValue(new SkinTextures(
                HYPERGAMY_SKIN,
                null,
                original.capeTexture(),
                original.elytraTexture(),
                original.model(),
                original.secure()
        ));
    }
}
