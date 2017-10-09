package models;

public class TexturedModel extends RawModel {

	private int texture;

	public TexturedModel(){

	}

	public TexturedModel(int vaoID, int vertexCount, int texture) {
		super(vaoID, vertexCount);
		this.texture = texture;
	}

	public TexturedModel(RawModel model, int texture){
		super(model.getVaoID(), model.getVertexCount());
		this.texture = texture;
	}

	public void set(int vaoID, int vertexCount, int texture){
		super.set(vaoID, vertexCount);
		this.texture = texture;
	}

	public void set(TexturedModel model){
		super.set(model.getVaoID(), model.getVertexCount());
		this.texture = model.texture;
	}

	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}
}
