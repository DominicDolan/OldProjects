package renderers.color;

import matrices.TransformationMatrix;
import matrices.ViewMatrix;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import renderers.Renderer;
import renderers.ShaderProgram;
import statics.W;

public class ColorRenderer extends Renderer {
	private float[] colorArray = new float[4];
	private Matrix4f transformationMatrix;
	private Matrix4f viewMatrix;
	private ColorShader shader;

	@Override
	public ShaderProgram getShader() {
		this.shader = new ColorShader();

		this.shader.start();
		this.shader.loadProjectionMatrix(W.projectionMatrix.create());
		setViewMatrix(W.viewMatrix);
//		this.shader.loadViewMatrix(W.viewMatrix.create());
		this.shader.stop();

		return this.shader;
	}

//	@Override
//	public RenderFunction defaultDraw() {
//		return new RenderFunction() {
//			@Override
//			public void draw(TexturedModel model) {
//				shader.setUniform4fv(shader.boxColor, colorArray);
//				shader.loadTransformationMatrix(transformationMatrix);
//
//				if (model.drawType==GL11.GL_TRIANGLES)
//					GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
//							GL11.GL_UNSIGNED_SHORT, 0);
//				else
//					GL11.glDrawArrays(model.drawType, 0,
//							model.getVertexCount());
//			}
//		};
//	}

	public void setColor(float[] colorArray) {
		this.colorArray = colorArray;
	}

	public void setTransformation(TransformationMatrix transformationMatrix) {
		this.transformationMatrix = transformationMatrix.create();
	}

	public void setViewMatrix(ViewMatrix matrix){
		this.viewMatrix = matrix.create();
	}

	public Matrix4f getTransformationMatrix() {
		return transformationMatrix;
	}

	public void loadUniforms(){
		shader.setUniform4fv(shader.color, colorArray);
		shader.loadTransformationMatrix(transformationMatrix);
	}

	public void defaultDraw(TexturedModel model){
		shader.loadViewMatrix(viewMatrix);
		if (model.drawType==GL11.GL_TRIANGLES)
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
					GL11.GL_UNSIGNED_SHORT, 0);
		else
			GL11.glDrawArrays(model.drawType, 0,
					model.getVertexCount());
	}

}
