package squal.panoramics;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class PanoramicsCubeMapRenderer extends CubeMapRenderer {
    private final Identifier[] faces = new Identifier[6];

    public PanoramicsCubeMapRenderer(Identifier faces) {
        super(faces);

        for (int i = 1; i < 7; ++i) {
            this.faces[i - 1] = faces.withPath(faces.getPath() + "_" + i);
        }
    }

    public void draw(MinecraftClient client, float x, float y, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Matrix4f matrix4f = new Matrix4f().setPerspective(1.4835298f,
                (float) client.getWindow().getFramebufferWidth() / (float) client.getWindow().getFramebufferHeight(),
                0.05f, 10.0f);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_DISTANCE);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.loadIdentity();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        for (int j = 0; j < 4; ++j) {
            matrixStack.push();
            float f = ((float) (j % 2) / 2.0f - 0.5f) / 256.0f;
            float g = ((float) (j / 2) / 2.0f - 0.5f) / 256.0f;
            matrixStack.translate(f, g, 0.0f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(x));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(y));
            RenderSystem.applyModelViewMatrix();
            for (int k = 0; k < 6; ++k) {
                RenderSystem.setShaderTexture(0, this.faces[k]);
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
                int l = Math.round(255.0f * alpha) / (j + 1);
                if (k == 0) {
                    bufferBuilder.vertex(-1.0, -1.0, 1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, 1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, 1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, 1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 1) {
                    bufferBuilder.vertex(1.0, -1.0, 1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, 1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, -1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, -1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 2) {
                    bufferBuilder.vertex(1.0, -1.0, -1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, -1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, -1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, -1.0, -1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 3) {
                    bufferBuilder.vertex(-1.0, -1.0, -1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, -1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, 1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, -1.0, 1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 4) {
                    bufferBuilder.vertex(-1.0, -1.0, -1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, -1.0, 1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, 1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, -1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 5) {
                    bufferBuilder.vertex(-1.0, 1.0, 1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, -1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, -1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, 1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                tessellator.draw();
            }
            matrixStack.pop();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.colorMask(true, true, true, false);
        }
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.restoreProjectionMatrix();
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
    }

    public CompletableFuture<Void> loadTexturesAsync(TextureManager textureManager, Executor executor) {
        CompletableFuture[] completableFutures = new CompletableFuture[6];
        for (int i = 0; i < completableFutures.length; ++i) {
            completableFutures[i] = textureManager.loadTextureAsync(this.faces[i], executor);
        }
        return CompletableFuture.allOf(completableFutures);
    }

}
