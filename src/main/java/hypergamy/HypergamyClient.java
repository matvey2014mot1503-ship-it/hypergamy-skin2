package hypergamy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class HypergamyClient implements ClientModInitializer {
    /** Включена ли панель. По умолчанию включена. */
    public static boolean enabled = true;

    private static KeyBinding toggleKey;

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("hypergamy.properties");
    }

    public static void load() {
        Path p = configPath();
        if (!Files.exists(p)) return;
        try (InputStream in = Files.newInputStream(p)) {
            Properties props = new Properties();
            props.load(in);
            enabled = Boolean.parseBoolean(props.getProperty("enabled", "true"));
        } catch (IOException ignored) {
        }
    }

    public static void save() {
        try (OutputStream out = Files.newOutputStream(configPath())) {
            Properties props = new Properties();
            props.setProperty("enabled", Boolean.toString(enabled));
            props.store(out, "Hypergamy Skin");
        } catch (IOException ignored) {
        }
    }

    @Override
    public void onInitializeClient() {
        load();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hypergamy.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.hypergamy"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                save();
                if (client.player != null) {
                    client.player.sendMessage(
                            Text.translatable(enabled ? "hypergamy.on" : "hypergamy.off"), true);
                }
            }
        });
    }
}
