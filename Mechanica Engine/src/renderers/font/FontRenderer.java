package renderers.font;


import font.GUIText;
import models.TexturedModel;
import renderers.RenderFunction;
import renderers.Renderer;
import renderers.ShaderProgram;
import statics.W;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class FontRenderer extends Renderer {

	private FontShader shader;
	private GUIText text;
	private float[] colorArray = new float[4];

	public FontRenderer(){
		super();
		setCustomRender(new RenderFunction() {
			@Override
			public void render(TexturedModel model) {
				defaultPrepare(model.getVaoID());
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				defaultDraw(model);
				defaultEnd();
			}
		});
	}

	@Override
	public ShaderProgram getShader() {
		this.shader = new FontShader();

		this.shader.start();
		this.shader.loadProjectionAndView(W.projectionMatrix, W.viewMatrix);
		this.shader.stop();

		return this.shader;
	}

	public void loadUniforms(){
		shader.loadColor(0xFFFF00FF);
//		shader.loadTranslation(text.getPositionX(), text.getPositionY());
		shader.loadProjectionAndView(W.projectionMatrix, W.UIView);
	}

	public void setText(GUIText text){
		this.text = text;
	}

	public void setColor(float r, float g, float b, float a){
		colorArray[0] = r;
		colorArray[1] = g;
		colorArray[2] = b;
		colorArray[3] = a;
	}

	public void setColor(float[] colorArray){
		System.arraycopy(colorArray, 0, this.colorArray, 0, 4);
	}
	
	public void defaultDraw(TexturedModel model){
		GUIText text = (GUIText) model;
		shader.loadTranslation(text.getPositionX(), text.getPositionY());
		shader.loadColor(colorArray[0], colorArray[1], colorArray[2], colorArray[3]);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture());
//		glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
//				GL11.GL_UNSIGNED_SHORT, 0);
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	}


}
