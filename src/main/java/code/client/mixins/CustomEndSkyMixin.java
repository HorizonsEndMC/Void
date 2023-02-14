package code.client.mixins;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import code.Void;

@Mixin({ WorldRenderer.class })
public class CustomEndSkyMixin {
    private static final Identifier CUSTOM_END_SKY = Void.id("textures/environment/nebula_skybox.png");

    /**
     * @author
     * @reason
     */

    @Overwrite
    private void renderEndSky(MatrixStack matrices) {
        if (matrices == null) {
            return;
        }
    try {
        RenderSystem.enableBlend();
        BackgroundRenderer.clearFog();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, CUSTOM_END_SKY);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float[] points = new float[8];
        for (int i = 0; i < 6; i++) {
            matrices.push();
            if (i == 0)
                points = new float[] { 0.33333334F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.33333334F, 0.5F };
            if (i == 1) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                points = new float[] { 0.6666667F, 0.5F, 0.6666667F, 1.0F, 1.0F, 1.0F, 1.0F, 0.5F };
            }
            if (i == 2) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                points = new float[] { 0.33333334F, 1.0F, 0.33333334F, 0.5F, 0.0F, 0.5F, 0.0F, 1.0F };
            }
            if (i == 3) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
                points = new float[] { 0.33333334F, 0.5F, 0.6666667F, 0.5F, 0.6666667F, 0.0F, 0.33333334F, 0.0F };
            }
            if (i == 4) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
                points = new float[] { 0.6666667F, 0.5F, 1.0F, 0.5F, 1.0F, 0.0F, 0.6666667F, 0.0F };
            }
            if (i == 5) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
                points = new float[] { 0.6666667F, 0.5F, 0.33333334F, 0.5F, 0.33333334F, 1.0F, 0.6666667F, 1.0F };
            }
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(points[0], points[1])
                    .color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(points[2], points[3])
                    .color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(points[4], points[5])
                    .color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(points[6], points[7])
                    .color(255, 255, 255, 255).next();
            tessellator.draw();
            matrices.pop();
        }

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
       // RenderSystem.disableBlend();
    } catch (Throwable throwable) {
        throwable.printStackTrace();
    }
    }
}
