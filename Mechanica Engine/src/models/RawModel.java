package models;

import org.lwjgl.opengl.GL11;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	public int drawType;

	public RawModel(){

	}

	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		drawType = GL11.GL_TRIANGLE_FAN;
	}

	public void set(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		drawType = GL11.GL_TRIANGLE_FAN;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}
}
