package renderers.background;

import renderers.WorldShader;

/**
 * Created by domin on 26 Apr 2017.
 */
public class BackgroundShader extends WorldShader {

    private static final String VERTEX_FILE = "src/renderers/background/backgroundVertex.txt";
    private static final String FRAGMENT_FILE = "src/renderers/background/backgroundFragment.txt";

    public int color;

    public BackgroundShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        getMatrixLocations();
        color = super.getUniformLocation("color");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
