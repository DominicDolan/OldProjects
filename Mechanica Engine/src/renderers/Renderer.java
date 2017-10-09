package renderers;

import models.TexturedModel;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by domin on 15 Apr 2017.
 */
public abstract class Renderer {
    protected ShaderProgram shader;
    protected RenderFunction function, defaultFunction;

    public Renderer()
    {
        init(getShader(), new RenderFunction() {

            @Override
            public void render(TexturedModel model) {
                defaultPrepare(model.getVaoID());
                defaultDraw(model);
                defaultEnd();
            }
        });
    }

    public abstract ShaderProgram getShader();

    public abstract void defaultDraw(TexturedModel model);

    public abstract void loadUniforms();

    private void init(ShaderProgram shader, RenderFunction function){
        this.shader = shader;
        this.function = function;
        this.defaultFunction = function;

    }

    public void setCustomRender(RenderFunction function){
        this.function = function;
    }

    public void setDefaultRender(){
        function = defaultFunction;
    }

    public void defaultPrepare(int vaoID){
        GL30.glBindVertexArray(vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }

//    protected abstract void defaultDraw(TexturedModel model);

    public void defaultEnd(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
    }

    public void render(TexturedModel model){
        shader.start();
        loadUniforms();
        function.render(model);
        shader.stop();
    }

    public void cleanUp(){
        shader.stop();
        shader.cleanUp();
    }
}
