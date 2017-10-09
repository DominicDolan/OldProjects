package matrices;

import org.lwjgl.util.vector.Matrix4f;
import statics.G;

/**
 * Created by domin on 23 Mar 2017.
 */

public class ProjectionMatrix {
    private Matrix4f matrix;

    private float fov = 70;
    private float nearPlane = 0.1f;
    private float farPlane = 1000;

    public boolean scheduleCreation = true;

    public ProjectionMatrix(){
        matrix = new Matrix4f();
        matrix.setIdentity();
        scheduleCreation = true;
    }

    public void setFov(float fov) {
        this.fov = fov;
        scheduleCreation = true;
    }

    public float getFov() {
        return fov;
    }

    public float getFarPlane() {
        return farPlane;
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
        scheduleCreation = true;
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
        scheduleCreation = true;
    }

    public Matrix4f create(){
        if (scheduleCreation) {
            double aspectRatio = G.RATIO;

            float y_scale = (float) ((1f/Math.tan(Math.toRadians(fov/2f)))*aspectRatio);
            float x_scale = (float) (y_scale/aspectRatio);
            float frustum_length = farPlane - nearPlane;
            //      0   1   2   3
            //  0 [ 0   1   2   3 ]
            //  1 [ 4   5   6   7 ]
            //  2 [ 8   9   10  11]
            //  3 [ 12  13  14  15]
            //

            matrix.m00 = x_scale;
            matrix.m11 = y_scale;
            matrix.m22 = -((farPlane + nearPlane) / frustum_length);
            matrix.m23 = -1;
            matrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
            matrix.m33 = 0;

//            matrix[0] = x_scale;
//            matrix[5] = y_scale;
//            matrix[10] = -((farPlane + nearPlane)/frustum_length);
//            matrix[11] = -1;
//            matrix[14] = -((2*nearPlane*farPlane)/frustum_length);
//            matrix[15] = 0;
        }
        scheduleCreation = false;
        return matrix;
    }
}
