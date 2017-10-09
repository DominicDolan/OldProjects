package renderers.framebuffer;

import renderers.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "framebuffer/contrastVertex.txt";
	private static final String FRAGMENT_FILE = "framebuffer/contrastFragment.txt";
	
	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
