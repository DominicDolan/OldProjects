package renderers.texture;

import renderers.WorldShader;

/**
 * Created by domin on 15 Apr 2017.
 */
public class TextureShader extends WorldShader {

    private static final String VERTEX_FILE = "texture/textureVertex.txt";
    private static final String FRAGMENT_FILE = "texture/textureFragment.txt";


    public TextureShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        getMatrixLocations();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");

    }
}
