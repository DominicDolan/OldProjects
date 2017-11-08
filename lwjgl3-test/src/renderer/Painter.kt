package renderer

import models.Model
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11

/**
 * Created by domin on 28/10/2017.
 */
interface Painter{
    var color: Long

    fun drawRect(x: Double, y: Double, width: Double, height: Double)

    fun drawCenteredRect(x: Double, y: Double, width: Double, height: Double)

    fun drawRotatedRect(centerX: Double, centerY: Double, width: Double, height: Double, degrees: Double)

    fun fillPolygon(model: Model)

    fun fillRotatedPolygon(model: Model, centerX: Double, centerY: Double, width: Double, height: Double, degrees: Double)

    fun fillPolygon(model: Model, x: Double, y: Double)

    fun fillPolygon(model: Model, x: Double, y: Double, scaleWidth: Double, scaleHeight: Double)

    fun fillScreenPolygon(model: Model, x: Double, y: Double, scaleWidth: Double, scaleHeight: Double)

    fun fillScreenPolygon(model: Model, x: Double, y: Double, scaleWidth: Double, scaleHeight: Double, degrees: Float)

    fun drawImage(image: Int)

    fun drawScreenImage(image: Int, x: Double, y: Double, width: Double, height: Double)
    fun drawCenteredScreenImage(image: Int, x: Double, y: Double, width: Double, height: Double)

    fun drawImage(image: Int, matrix: Matrix4f)

    fun drawImage(image: Int, x: Double, y: Double, width: Double, height: Double)

    fun drawCenteredImage(image: Int, x: Double, y: Double, width: Double, height: Double)

    fun drawPositionalImage(image: Int, left: Double, top: Double, right: Double, bottom: Double)

    fun drawRotatedImage(image: Int, centerX: Double, centerY: Double, width: Double, height: Double, degrees: Double)

//    fun drawText(text: GUIText)
//
//    fun drawText(text: String, fontSize: Float, font: FontType, x: Float, y: Float, maxLineLength: Float, centered: Boolean)

    fun drawText(text: String, fontSize: Double, x: Double, y: Double)

    fun drawLine(strokeWidth: Double, x1: Double, y1: Double, x2: Double, y2: Double)

    fun drawHorizontalLine(strokeWidth: Double, y: Double)

    fun drawVerticalLine(strokeWidth: Double, x: Double)

    fun drawPositionalRect(left: Double, top: Double, right: Double, bottom: Double)
    fun drawScreenRect(x: Double, y: Double, width: Double, height: Double)
    fun drawCircle(centerX: Double, centerY: Double, radius: Double, strokeWidth: Double)
    fun fillCircle(centerX: Double, centerY: Double, radius: Double)
    fun drawEllipse(centerX: Double, centerY: Double, horizontalAxis: Double, verticalAxis: Double, strokeWidth: Double)
    fun fillEllipse(centerX: Double, centerY: Double, horizontalAxis: Double, verticalAxis: Double)
    fun fillRotatedEllipse(centerX: Double, centerY: Double, horizontalAxis: Double, verticalAxis: Double, degrees: Double)
}