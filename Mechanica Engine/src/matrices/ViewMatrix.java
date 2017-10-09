package matrices;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by domin on 23 Mar 2017.
 */

public class ViewMatrix {
    private Matrix4f matrix;
    private Matrix4f matrixWithProjection;
    private Vector3f translation;
    private Vector3f xAxis = new Vector3f(1,0,0);
    private Vector3f yAxis = new Vector3f(0,1,0);
    private Vector3f zAxis = new Vector3f(0,0,1);
    private float positiionX = 0, positionY = 0, positionZ = 0;
    private float rx = 0, ry = 0, rz = 0;

    private boolean scheduleCreation = true;
    private boolean scheduleProjectionAndView = true;

    public ViewMatrix(){
        translation = new Vector3f();
        matrix = new Matrix4f();
        matrix.setIdentity();
        matrixWithProjection = new Matrix4f();
        matrixWithProjection.setIdentity();
        scheduleCreation = true;
    }

    public void setTranslate(float x, float y, float z){
        translation.set((float) - x, (float) - y, (float) - z);
        scheduleCreation = true;
    }

    public void setRotate(float x, float y, float z){
        rx = x;
        ry = y;
        rz = z;
        scheduleCreation = true;
    }

    public Matrix4f create(){
        if (scheduleCreation) {
            matrix.setIdentity();
            Matrix4f.translate(translation, matrix, matrix);

            Matrix4f.rotate((float) Math.toRadians(rx), xAxis, matrix, matrix);
            Matrix4f.rotate((float) Math.toRadians(ry), yAxis, matrix, matrix);
            Matrix4f.rotate((float) Math.toRadians(rz), zAxis, matrix, matrix);

            scheduleProjectionAndView = true;
        }
        scheduleCreation = false;
        return matrix;
    }

    public Matrix4f createWithProjection(ProjectionMatrix projectionMatrix){
        if (scheduleCreation || projectionMatrix.scheduleCreation || scheduleProjectionAndView) {
            matrix.setIdentity();
            Matrix4f.translate(translation, matrix, matrix);

            Matrix4f.rotate((float) Math.toRadians(rx), xAxis, matrix, matrix);
            Matrix4f.rotate((float) Math.toRadians(ry), yAxis, matrix, matrix);
            Matrix4f.rotate((float) Math.toRadians(rz), zAxis, matrix, matrix);

            Matrix4f.mul( projectionMatrix.create(), matrix, matrixWithProjection);
        }
        scheduleProjectionAndView = false;
        scheduleCreation = false;
        return matrixWithProjection;
    }
}
