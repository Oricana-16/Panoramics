package squal.panoramics;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Panoramics implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("panoramics");

	public static final String ScrDir = FabricLoader.getInstance().getGameDir().toString() + "/screenshots/panoramics/";

	@Override
	public void onInitializeClient() {

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
				ClientCommandManager.literal("panorama").executes(context -> {
					MinecraftClient client = MinecraftClient.getInstance();

					LocalDateTime myDateObj = LocalDateTime.now();

					DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd_MM-HH_mm");

					String formattedDate = myDateObj.format(myFormatObj);

					File dir = new File(ScrDir + formattedDate + "/");
					dir.mkdir();

					client.player.sendMessage(client.takePanorama(dir, 1024, 1024));
					return 1;
				})));

	}
}