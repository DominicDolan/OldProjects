package renderers.background;

import matrices.TransformationMatrix;
import matrices.ViewMatrix;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import renderers.ShaderProgram;
import renderers.texture.TextureRenderer;
import statics.W;

/**
 * Created by domin on 26 Apr 2017.
 */
public class BackgroundRenderer extends TextureRenderer {
    private BackgroundShader shader;
    private Matrix4f transformationMatrix;
    private Matrix4f viewMatrix, projectionMatrix;
    @Override
    public ShaderProgram getShader() {
        shader = new BackgroundShader();

        this.shader.start();
        this.shader.loadProjectionMatrix(W.projectionMatrix.create());
        this.shader.loadViewMatrix(W.viewMatrix.create());
        this.shader.stop();

        return shader;
    }


    public void setTransformation(TransformationMatrix transformationMatrix) {
        this.transformationMatrix = transformationMatrix.create();
    }


    public void setViewMatrix(ViewMatrix matrix){
        this.viewMatrix = matrix.create();
    }

    public void setTransformation(Matrix4f transformationMatrix) {
        this.transformationMatrix = transformationMatrix;
    }

    @Override
    public void defaultDraw(TexturedModel model) {
        shader.loadViewMatrix(viewMatrix);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
                GL11.GL_UNSIGNED_SHORT, 0);
    }

    @Override
    public void loadUniforms() {
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
