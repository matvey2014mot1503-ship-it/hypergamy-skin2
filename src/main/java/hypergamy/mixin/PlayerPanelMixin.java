package hypergamy.mixin;

import hypergamy.HypergamyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Вместо объёмной модели игрока рисует плоскую панель с картинкой.
 * Имя над головой сохраняется.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class PlayerPanelMixin {
    @Unique
    private static final Identifier HYPERGAMY_PANEL = Identifier.of("hypergamy", "textures/panel.png");

    // Размеры панели в блоках (исходная картинка 828x979)
    @Unique
    private static final float PANEL_HEIGHT = 1.8F;
    @Unique
    private static final float PANEL_WIDTH = 1.5F;

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hypergamy$renderPanel(LivingEntityRenderState state, MatrixStack matrices,
                                       VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!HypergamyClient.enabled || !(state instanceof PlayerEntityRenderState)) {
            return;
        }

        PlayerEntityRenderState player = (PlayerEntityRenderState) state;

        matrices.push();
        // Панель всегда смотрит на камеру (вращение только вокруг вертикальной оси)
        float cameraYaw = MinecraftClient.getInstance().gameRenderer.getCamera().getYaw();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-cameraYaw));

        VertexConsumer buf = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(HYPERGAMY_PANEL));
        MatrixStack.Entry e = matrices.peek();
        float hw = PANEL_WIDTH / 2.0F;
        float h = PANEL_HEIGHT;

        // Лицевая сторона (смотрит туда же, куда игрок): слева направо = от +X к -X
        vertex(buf, e, hw, 0, 0, 0.0F, 1.0F, light, 0, 0, -1);
        vertex(buf, e, -hw, 0, 0, 1.0F, 1.0F, light, 0, 0, -1);
        vertex(buf, e, -hw, h, 0, 1.0F, 0.0F, light, 0, 0, -1);
        vertex(buf, e, hw, h, 0, 0.0F, 0.0F, light, 0, 0, -1);

        // Обратная сторона, чтобы картинка читалась и сзади
        vertex(buf, e, -hw, 0, 0, 0.0F, 1.0F, light, 0, 0, 1);
        vertex(buf, e, hw, 0, 0, 1.0F, 1.0F, light, 0, 0, 1);
        vertex(buf, e, hw, h, 0, 1.0F, 0.0F, light, 0, 0, 1);
        vertex(buf, e, -hw, h, 0, 0.0F, 0.0F, light, 0, 0, 1);

        // Предметы в руках: правая рука со стороны +X (слева на экране), левая со стороны -X
        renderHeldItem(player.rightHandItemState, matrices, vertexConsumers, light, hw - 0.1F);
        renderHeldItem(player.leftHandItemState, matrices, vertexConsumers, light, -(hw - 0.1F));

        matrices.pop();

        // Имя игрока над головой, как в обычной отрисовке
        if (state.displayName != null) {
            ((RenderLabelInvoker) (Object) this).hypergamy$renderLabel(
                    state, state.displayName, matrices, vertexConsumers, light);
        }

        ci.cancel();
    }

    @Unique
    private static void renderHeldItem(ItemRenderState itemState, MatrixStack matrices,
                                       VertexConsumerProvider vertexConsumers, int light, float x) {
        if (itemState.isEmpty()) {
            return;
        }
        matrices.push();
        // чуть перед панелью, на уровне рук
        matrices.translate(x, 0.55F, -0.12F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.scale(0.8F, 0.8F, 0.8F);
        itemState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }

    @Unique
    private static void vertex(VertexConsumer buf, MatrixStack.Entry e, float x, float y, float z,
                               float u, float v, int light, float nx, float ny, float nz) {
        buf.vertex(e, x, y, z)
                .color(255, 255, 255, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(e, nx, ny, nz);
    }
}
