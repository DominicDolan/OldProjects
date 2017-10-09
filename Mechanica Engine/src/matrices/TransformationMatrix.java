package matrices;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by domin on 23 Mar 2017.
 */

public class TransformationMatrix {
    private Matrix4f matrix;
    private Vector3f translation;
    private Vector3f pivot, unpivot;
    private Vector3f scale;
    private Vector3f xAxis = new Vector3f(1,0,0);
    private Vector3f yAxis = new Vector3f(0,1,0);
    private Vector3f zAxis = new Vector3f(0,0,1);
    private float rx = 0, ry = 0, rz = 0;
    private float scaleX = 1, scaleY = 1, scaleZ = 1;

    private boolean scheduleRotation = true;

    public TransformationMatrix(){
        translation = new Vector3f();
        pivot = new Vector3f();
        unpivot = new Vector3f();
        scale = new Vector3f(1,1,1);
        matrix = new Matrix4f();
        matrix.setIdentity();
    }

    public void setTranslate(double x, double y, double z){
        translation.set((float) x, (float) y, (float) z);
    }

    public void setRotate(double x, double y, double z){
        rx = (float) x;
        ry = (float) y;
        rz = (float) z;
        scheduleRotation = true;
    }

    public void setScale(double x, double y, double z){
        scale.set((float) x, (float) y, (float) z);
    }

    public float getScaleX(){
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setPivot(double pivotX, double pivotY) {
        pivot.set((float) pivotX, (float) pivotY, 0);
        unpivot.set((float) -pivotX, (float) -pivotY, 0);
        scheduleRotation = true;
    }

    public Matrix4f create(){
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        if (scheduleRotation) {
            if (pivot.getX() != 0 || pivot.getY() !=0)
                Matrix4f.translate(pivot, matrix, matrix);

            Matrix4f.rotate((float) Math.toRadians(rx), xAxis, matrix, matrix);
            Matrix4f.rotate((float) Math.toRadians(ry), yAxis, matrix, matrix);
            Matrix4f.rotate((float) Math.toRadians(rz), zAxis, matrix, matrix);

            if (pivot.getX() != 0 || pivot.getY() !=0)
                Matrix4f.translate(unpivot, matrix, matrix);
        }
        scheduleRotation = false;
        Matrix4f.scale(scale, matrix, matrix);
        pivot.set(0,0,0);
        unpivot.set(0,0,0);
        rx =0; ry = 0; rz = 0;
        return matrix;
    }

    public void rewind(){
        matrix.setIdentity();
        setRotate(0,0,0);
        setTranslate(0,0,0);
        setScale(1,1,1);
        setPivot(0,0);
    }


}
