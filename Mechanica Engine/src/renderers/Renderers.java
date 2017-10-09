package renderers;

import font.GUIText;
import matrices.TransformationMatrix;
import matrices.ViewMatrix;
import org.lwjgl.util.vector.Matrix4f;
import renderers.color.ColorShader;
import renderers.font.FontShader;
import renderers.texture.TextureShader;
import statics.W;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static renderers.DefaultRenderer.*;

/**
 * Created by domin on 28 Apr 2017.
 */
public class Renderers {
    public static RenderFunction colorRenderer, textureRenderer, customMatrixRenderer, fontRenderer;
    public static ColorShader colorShader;
    public static TextureShader textureShader;
    public static FontShader fontShader;

    public static float[] colorArray;
    public static TransformationMatrix transformationMatrix;
    public static ViewMatrix viewMatrix;
    public static Matrix4f matrix4f;

    public static void makeShaders(){
        textureShader = new TextureShader();
        textureShader.start();
        textureShader.loadProjectionMatrix(W.projectionMatrix.create());
        textureShader.loadViewMatrix(W.viewMatrix.create());
        textureShader.stop();

        colorShader = new ColorShader();
        colorShader.start();
        colorShader.loadProjectionMatrix(W.projectionMatrix.create());
        colorShader.loadViewMatrix(W.viewMatrix.create());
        colorShader.stop();

        fontShader = new FontShader();
        fontShader.start();
        fontShader.loadProjectionAndView(W.projectionMatrix, W.viewMatrix);
        fontShader.stop();

        colorArray = new float[4];
        transformationMatrix = new TransformationMatrix();
        viewMatrix = W.viewMatrix;
    }

    public static void makeRenderers(){
        colorRenderer = (model) -> {
            prepareVertexArrays(model.getVaoID(), 1);

            colorShader.start();
            colorShader.loadViewMatrix(viewMatrix.create());
            colorShader.loadTransformationMatrix(transformationMatrix.create());
            colorShader.setUniform4fv(colorShader.color, colorArray);

            if (model.drawType== GL_TRIANGLES)
                glDrawElements(GL_TRIANGLES, model.getVertexCount(),
                        GL_UNSIGNED_SHORT, 0);
            else
                glDrawArrays(model.drawType, 0,
                        model.getVertexCount());
            
            colorShader.stop();

            disableVertexArrays();
        };

        textureRenderer = (model) -> {
            prepareVertexArrays(model.getVaoID(), 2);

            textureShader.start();
            textureShader.loadViewMatrix(viewMatrix.create());
            textureShader.loadTransformationMatrix(transformationMatrix.create());

            enableAlphaBlending();
            drawTexture(model);

            disableVertexArrays();
            textureShader.stop();
        };

        fontRenderer = (model) -> {
            prepareVertexArrays(model.getVaoID(), 2);
            enableAlphaBlending();

            GUIText text = (GUIText) model;

            fontShader.start();
            fontShader.loadTranslation(text.getPositionX(), text.getPositionY());
            fontShader.loadColor(colorArray[0], colorArray[1], colorArray[2], colorArray[3]);
            fontShader.loadProjectionAndView(W.projectionMatrix, W.UIView);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, model.getTexture());
//		glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
//				GL11.GL_UNSIGNED_SHORT, 0);
            glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());

            disableVertexArrays();
            fontShader.stop();
        };

        customMatrixRenderer = (model) -> {
            prepareVertexArrays(model.getVaoID(), 2);

            textureShader.start();
            textureShader.loadViewMatrix(viewMatrix.create());
            textureShader.loadTransformationMatrix(matrix4f);

            enableAlphaBlending();
            drawTexture(model);

            disableVertexArrays();
            textureShader.stop();
        };
    }

    public static void cleanUp(){
        textureShader.cleanUp();
        colorShader.cleanUp();
        fontShader.cleanUp();
    }
}
