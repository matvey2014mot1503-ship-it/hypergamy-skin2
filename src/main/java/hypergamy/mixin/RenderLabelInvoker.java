package hypergamy.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface RenderLabelInvoker {
    @Invoker("renderLabelIfPresent")
    void hypergamy$renderLabel(EntityRenderState state, Text text, MatrixStack matrices,
                               VertexConsumerProvider vertexConsumers, int light);
}
