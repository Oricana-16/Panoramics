package squal.panoramics.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import squal.panoramics.Panoramics;
import squal.panoramics.PanoramicsCubeMapRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RotatingCubeMapRenderer.class)
public class RotatingCubeMapRendererMixin {



  @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
  private static CubeMapRenderer setCubeMap(CubeMapRenderer cubemap) throws IOException {

    File f = new File(Panoramics.ScrDir);
    f.mkdir();
    File[] files = f.listFiles();

    if (files == null || files.length == 0) {
      Panoramics.LOGGER.info("[PANORAMICS] No Panoramics Found, skipping");
      return cubemap;
    }

    Random randomizer = new Random();

    File topFolder = files[randomizer.nextInt(files.length)];
    // The [0].listFiles() is because TakePanorama() saves it under a subfolder
    // "screenshots"
    File[] chosenFolder = topFolder.listFiles()[0].listFiles();

    for (int i = 0; i < chosenFolder.length; i++) {
      File curP = chosenFolder[i];

      NativeImage nativeImage = NativeImage.read(Files.newInputStream(curP.toPath()));
      MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(topFolder.getName(),
          new NativeImageBackedTexture(nativeImage));

      nativeImage.loadFromTextureImage(0, true);
      nativeImage.mirrorVertically();
    }

    return new PanoramicsCubeMapRenderer(new Identifier("dynamic/" + topFolder.getName()));
  }
}