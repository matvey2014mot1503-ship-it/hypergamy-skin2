package hypergamy;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private ButtonWidget toggleButton;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("hypergamy.config.title"));
        this.parent = parent;
    }

    private Text toggleText() {
        return Text.translatable(HypergamyClient.enabled ? "hypergamy.config.on" : "hypergamy.config.off");
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        toggleButton = ButtonWidget.builder(toggleText(), button -> {
            HypergamyClient.enabled = !HypergamyClient.enabled;
            HypergamyClient.save();
            button.setMessage(toggleText());
        }).dimensions(cx - 100, this.height / 2 - 10, 200, 20).build();
        this.addDrawableChild(toggleButton);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close())
                .dimensions(cx - 100, this.height / 2 + 20, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
