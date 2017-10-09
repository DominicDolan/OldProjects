package renderers.font;

import matrices.ProjectionMatrix;
import matrices.ViewMatrix;
import renderers.ShaderProgram;
import statics.F;

public class FontShader extends ShaderProgram {
	private final float ratio = (float) (1);

	public int position;
	public int textureCoords;
	public int translation;
	public int projectionAndView;
	public int scale;
	public int color;


	private static final String VERTEX_FILE = "font/fontVertex.txt";
	private static final String FRAGMENT_FILE = "font/fontFragment.txt";


	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		loadRatio();
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1,"textureCoords");
	}

	@Override
	protected void getAllUniformLocations(){
		translation = getUniformLocation("translation");
		color = getUniformLocation("color");
		scale = getUniformLocation("scale");
		projectionAndView = getUniformLocation("projectionAndView");
	}


	public void loadProjectionAndView(ProjectionMatrix projectionMatrix, ViewMatrix viewMatrix){
		loadMatrix(this.projectionAndView, viewMatrix.createWithProjection(projectionMatrix));
	}

	public void loadColor(int color){
		F.splitColor(color);
		loadVector(this.color, F.r, F.g, F.b, F.a);
	}

	public void loadColor(float r, float g, float b, float a){
		loadVector(this.color, r, g, b, a);
	}

	public void loadTranslation(float x, float y){
		loadVector(translation, x, y);
	}

	private void loadRatio(){
		loadFloat(scale, ratio);
	}
}
