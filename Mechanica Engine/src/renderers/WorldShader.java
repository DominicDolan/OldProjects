package renderers;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by domin on 15 Apr 2017.
 */
public abstract class WorldShader extends ShaderProgram {

    protected int transformationMatrix;
    protected int projectionMatrix;
    protected int viewMatrix;

    public WorldShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    public void getMatrixLocations(){
        transformationMatrix = super.getUniformLocation("transformationMatrix");
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        viewMatrix = super.getUniformLocation("viewMatrix");
    }

    protected static String getPath(){
        return WorldShader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(projectionMatrix, projection);
    }

    public void loadViewMatrix(Matrix4f view){
        super.loadMatrix(viewMatrix, view);
    }


}
