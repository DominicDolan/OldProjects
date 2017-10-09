package renderers;

import models.TexturedModel;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by domin on 28 Apr 2017.
 */
public class DefaultRenderer {
    private static int activeVBOs;
    public static void prepareVertexArrays(int vaoID, int vboCount){
        GL30.glBindVertexArray(vaoID);
        activeVBOs = vboCount;
        for (int i = 0; i < vboCount; i++) {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    public static void disableVertexArrays(){
        for (int i = 0; i < activeVBOs; i++) {
            GL20.glDisableVertexAttribArray(i);
        }
        activeVBOs = 0;

        GL30.glBindVertexArray(0);
    }

    public static void enableAlphaBlending(){
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void drawTexture(TexturedModel model){
        glBindTexture(GL_TEXTURE_2D, model.getTexture());
        glDrawElements(GL_TRIANGLES, model.getVertexCount(),
                GL_UNSIGNED_SHORT, 0);
    }
}
