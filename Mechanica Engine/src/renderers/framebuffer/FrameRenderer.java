package renderers.framebuffer;

import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import renderers.Renderer;
import renderers.ShaderProgram;

public class FrameRenderer extends Renderer {
	private ContrastShader shader;
	private Fbo fbo;

	public FrameRenderer(int width, int height) {
		this.fbo = new Fbo(width, height);
	}

	public FrameRenderer(Fbo fbo) {
		this.fbo = fbo;
	}

	@Override
	public ShaderProgram getShader() {
		shader = new ContrastShader();
		return shader;
	}

	@Override
	public void defaultPrepare(int vaoID) {
		super.defaultPrepare(vaoID);
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
	}

	@Override
	public void defaultEnd() {
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
		super.defaultEnd();
	}

	@Override
	public void defaultDraw(TexturedModel model) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture());
		if (model.drawType==GL11.GL_TRIANGLES)
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
					GL11.GL_UNSIGNED_SHORT, 0);
		else
			GL11.glDrawArrays(model.drawType, 0,
					model.getVertexCount());


	}

	@Override
	public void loadUniforms() {

	}

//	public void render(TexturedModel model) {
//
//	}

	public int getOutputTexture() {
		return fbo.getColourTexture();
	}

	public void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}

}
