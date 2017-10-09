package renderers.color;


import renderers.WorldShader;

public class ColorShader extends WorldShader {
	
	private static final String VERTEX_FILE = "color/colorVertex.txt";
	private static final String FRAGMENT_FILE = "color/colorFragment.txt";

	public int color;

	public ColorShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		getMatrixLocations();
		color = super.getUniformLocation("color");
		
	}

	

}
