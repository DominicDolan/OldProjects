package renderers.texture;

import matrices.TransformationMatrix;
import matrices.ViewMatrix;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import renderers.Renderer;
import renderers.ShaderProgram;
import statics.W;

/**
 * Created by domin on 15 Apr 2017.
 */
public class TextureRenderer extends Renderer {
    private TextureShader shader;
    private Matrix4f transformationMatrix;
    private Matrix4f viewMatrix, projectionMatrix;
    @Override
    public ShaderProgram getShader() {
        this.shader = new TextureShader();

        this.shader.start();
        this.shader.loadProjectionMatrix(W.projectionMatrix.create());
        this.shader.loadViewMatrix(W.viewMatrix.create());
        this.shader.stop();

        return this.shader;
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

//    @Override
//    public RenderFunction defaultDraw() {
//        return new RenderFunction() {
//            @Override
//            public void draw(TexturedModel model) {
//                shader.loadTransformationMatrix(transformationMatrix);
//                GL11.glEnable(GL11.GL_BLEND);
//                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
//                GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture());
//                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
//                            GL11.GL_UNSIGNED_SHORT, 0);
//            }
//        };
//    }

    public void loadUniforms(){
        shader.loadTransformationMatrix(transformationMatrix);
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
}
