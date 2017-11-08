package matrices


import display.Game
import org.joml.Matrix4f

/**
 * Created by domin on 23 Mar 2017.
 */

class ProjectionMatrix {
    private val matrix: Matrix4f

    var fov = 70f
        set(fov) {
            field = fov
            scheduleCreation = true
        }
    private var nearPlane = 0.1f
    var farPlane = 1000f
        set(farPlane) {
            field = farPlane
            scheduleCreation = true
        }

    var scheduleCreation = true

    init {
        matrix = Matrix4f()
        matrix.identity()
        scheduleCreation = true
    }

    fun setNearPlane(nearPlane: Float) {
        this.nearPlane = nearPlane
        scheduleCreation = true
    }

    fun create(): Matrix4f {
        if (scheduleCreation) {
            val aspectRatio = Game.ratio

            val y_scale = (1f / Math.tan(Math.toRadians((this.fov / 2f).toDouble())) * aspectRatio).toFloat()
            val x_scale = (y_scale / aspectRatio).toFloat()
            val frustum_length = this.farPlane - nearPlane
            //      0   1   2   3
            //  0 [ 0   1   2   3 ]
            //  1 [ 4   5   6   7 ]
            //  2 [ 8   9   10  11]
            //  3 [ 12  13  14  15]
            //

            matrix.m00(x_scale)
            matrix.m11(y_scale)
            matrix.m22(-((this.farPlane + nearPlane) / frustum_length))
            matrix.m23(-1f)
            matrix.m32(-(2f * nearPlane * this.farPlane / frustum_length))
            matrix.m33(0f)

            //            matrix[0] = x_scale;
            //            matrix[5] = y_scale;
            //            matrix[10] = -((farPlane + nearPlane)/frustum_length);
            //            matrix[11] = -1;
            //            matrix[14] = -((2*nearPlane*farPlane)/frustum_length);
            //            matrix[15] = 0;
        }
        scheduleCreation = false
        return matrix
    }
}
