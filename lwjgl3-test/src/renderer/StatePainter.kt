package renderer

import display.Game
import font.FontType
import font.GUIText
import loader.loadFont
import loader.loadTexturedQuad
import models.Model
import org.joml.Matrix4f

/**
 * Created by domin on 28/10/2017.
 */
class StatePainter : Painter {
    var colorRenderer: (Model) -> Unit = renderer.colorRenderer
    var textureRenderer: (Model) -> Unit = renderer.textureRenderer
    var circleRenderer: () -> Unit = renderer.circleRenderer
    override var color: Long = 0xFFFFFFFF
        set(value) {
            var color = value
//        this.boxColor = boxColor;
            val a = color and 0xFF
            color = color - a shr 8
            val b = color and 0xFF
            color = color - b shr 8
            val g = color and 0xFF
            color = color - g shr 8
            val r = color and 0xFF
            colorArray[0] = r / 255f
            colorArray[1] = g / 255f
            colorArray[2] = b / 255f
            colorArray[3] = a / 255f

            field = value
        }

    val font: FontType = loadFont("arial")
    val guiText = GUIText("", 10f, font, 0f, 0f, 5f)
    val model = loadTexturedQuad(0, 0f, 1f, 1f, 0f)


    override fun drawRect(x: Double, y: Double, width: Double, height: Double) {
        transformationMatrix.setTranslate(x, y, 0.0)
        transformationMatrix.setScale(width, height, 1.0)
        colorRenderer(model)
    }

    override fun drawCenteredRect(x: Double, y: Double, width: Double, height: Double) {
        drawRect(x - width/2.0, y - height/2.0, width, height)
    }

    override fun drawPositionalRect(left: Double, top: Double, right: Double, bottom: Double){
        drawRect(left, bottom, right - left, top - bottom)
    }

    override fun drawRotatedRect(centerX: Double, centerY: Double, width: Double, height: Double, degrees: Double) {
        transformationMatrix.setPivot(width / 2, height / 2)
        transformationMatrix.setTranslate(centerX - width / 2, centerY - height / 2, 0.0)
        transformationMatrix.setRotate(0.0, 0.0, degrees)
        transformationMatrix.setScale(width, height, 1.0)
        colorRenderer(model)
        transformationMatrix.setRotate(0.0, 0.0, 0.0)
    }

    override fun drawScreenRect(x: Double, y: Double, width: Double, height: Double){
        transformationMatrix.setTranslate(x, y, 0.0)
        transformationMatrix.setScale(width, height, 1.0)
        renderer.drawingViewMatrix = Game.uiViewMatrix
        colorRenderer(model)
        renderer.drawingViewMatrix = Game.viewMatrix
    }

    override fun fillPolygon(model: Model) {
        transformationMatrix.rewind()
        colorRenderer(model)
    }

    override fun fillRotatedPolygon(model: Model, centerX: Double, centerY: Double, width: Double, height: Double, degrees: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fillPolygon(model: Model, x: Double, y: Double) {
        transformationMatrix.setTranslate(x, y, 0.0)
        colorRenderer(model)
    }

    override fun fillPolygon(model: Model, x: Double, y: Double, scaleWidth: Double, scaleHeight: Double) {
        transformationMatrix.setTranslate(x, y, 0.0)
        transformationMatrix.setScale(scaleWidth, scaleHeight, 1.0)
        colorRenderer(model)
    }

    override fun fillScreenPolygon(model: Model, x: Double, y: Double, scaleWidth: Double, scaleHeight: Double) {
        transformationMatrix.setTranslate(x, y, 0.0)
        transformationMatrix.setScale(scaleWidth, scaleHeight, 1.0)
        renderer.drawingViewMatrix = Game.uiViewMatrix
        colorRenderer(model)
        renderer.drawingViewMatrix = Game.viewMatrix
    }

    override fun fillScreenPolygon(model: Model, x: Double, y: Double, scaleWidth: Double, scaleHeight: Double, degrees: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun drawImage(image: Int) {
        model.texture = image
        textureRenderer(model)
    }

    override fun drawScreenImage(image: Int, x: Double, y: Double, width: Double, height: Double) {
        transformationMatrix.setTranslate(x, y, 0.0)
        transformationMatrix.setScale(width, height, 1.0)
        model.texture = image
        renderer.drawingViewMatrix = Game.uiViewMatrix
        textureRenderer(model)
        renderer.drawingViewMatrix = Game.viewMatrix
    }

    override fun drawCenteredScreenImage(image: Int, x: Double, y: Double, width: Double, height: Double) {
        transformationMatrix.setTranslate(x - width/2.0, y - height/2.0, 0.0)
        transformationMatrix.setScale(width, height, 1.0)
        model.texture = image
        renderer.drawingViewMatrix = Game.uiViewMatrix
        textureRenderer(model)
        renderer.drawingViewMatrix = Game.viewMatrix
    }

    override fun drawImage(image: Int, matrix: Matrix4f) {
        transformationMatrix.setMatrix(matrix)
        model.texture = image
        textureRenderer(model)
    }

    override fun drawImage(image: Int, x: Double, y: Double, width: Double, height: Double) {
        transformationMatrix.setTranslate(x, y, 0.0)
        transformationMatrix.setScale(width, height, 1.0)
        model.texture = image
        textureRenderer(model)
    }

    override fun drawCenteredImage(image: Int, x: Double, y: Double, width: Double, height: Double) {
        drawImage(image, x - width/2.0, y - height/2.0, width, height)
    }

    override fun drawPositionalImage(image: Int, left: Double, top: Double, right: Double, bottom: Double) {
        drawImage(image, left, bottom, right - left, top - bottom)
    }

    override fun drawRotatedImage(image: Int, centerX: Double, centerY: Double, width: Double, height: Double, degrees: Double) {
        transformationMatrix.setPivot(width / 2, height / 2)
        transformationMatrix.setTranslate(centerX - width / 2, centerY - height / 2, 0.0)
        transformationMatrix.setRotate(0.0, 0.0, degrees)
        transformationMatrix.setScale(width, height, 1.0)
        colorRenderer(model)
        transformationMatrix.setRotate(0.0, 0.0, 0.0)
    }

    override fun drawText(text: String, fontSize: Double, x: Double, y: Double) {
        guiText.set(text, fontSize.toFloat(), font, x.toFloat(), y.toFloat(), guiText.maxLineSize, guiText.isCentered)
        renderer.fontRenderer(guiText)
    }

    override fun drawCircle(centerX: Double, centerY: Double, radius: Double, strokeWidth: Double){
        transformationMatrix.setScale(radius*2, radius*2, 1.0)
        transformationMatrix.setTranslate(centerX - radius, centerY - radius, 0.0)
        renderer.strokeWidth = strokeWidth/radius
        renderer.circleRenderer()
    }

    override fun fillCircle(centerX: Double, centerY: Double, radius: Double){
        transformationMatrix.setScale(radius*2, radius*2, 1.0)
        transformationMatrix.setTranslate(centerX - radius, centerY - radius, 0.0)
        renderer.strokeWidth = 1.0
        renderer.circleRenderer()
    }

    override fun drawEllipse(centerX: Double, centerY: Double, horizontalAxis: Double, verticalAxis: Double, strokeWidth: Double){
        transformationMatrix.setScale(horizontalAxis, verticalAxis, 1.0)
        transformationMatrix.setTranslate(centerX - horizontalAxis/2.0, centerY - verticalAxis/2.0, 0.0)
        renderer.strokeWidth = 4.0*strokeWidth/(horizontalAxis + verticalAxis)
        renderer.circleRenderer()
    }

    override fun fillEllipse(centerX: Double, centerY: Double, horizontalAxis: Double, verticalAxis: Double){
        transformationMatrix.setScale(horizontalAxis, verticalAxis, 1.0)
        transformationMatrix.setTranslate(centerX - horizontalAxis/2.0, centerY - verticalAxis/2.0, 0.0)
        renderer.strokeWidth = 1.0
        renderer.circleRenderer()
    }

    override fun fillRotatedEllipse(centerX: Double, centerY: Double, horizontalAxis: Double, verticalAxis: Double, degrees: Double){
        transformationMatrix.setPivot(horizontalAxis / 2, verticalAxis / 2)
        transformationMatrix.setScale(horizontalAxis, verticalAxis, 1.0)
        transformationMatrix.setTranslate(centerX - horizontalAxis/2.0, centerY - verticalAxis/2.0, 0.0)
        transformationMatrix.setRotate(0.0, 0.0, degrees)
        renderer.strokeWidth = 1.0
        renderer.circleRenderer()
        transformationMatrix.setRotate(0.0, 0.0, 0.0)

    }

    override fun drawLine(strokeWidth: Double, x1: Double, y1: Double, x2: Double, y2: Double) {
        val triangleWidth = x2 - x1
        val triangleHeight = y2 - y1
        transformationMatrix.setScale(Math.hypot(triangleWidth, triangleHeight), strokeWidth, 1.0)

        transformationMatrix.setTranslate(x1, y1, 0.0)
        transformationMatrix.setPivot(0.0, strokeWidth / 2)
        transformationMatrix.setRotate(0.0, 0.0, Math.toDegrees(Math.atan2(triangleHeight, triangleWidth)))

        colorRenderer(model)
    }

    override fun drawHorizontalLine(strokeWidth: Double, y: Double) {
        transformationMatrix.setScale(Game.viewWidth, strokeWidth, 0.0)
        transformationMatrix.setTranslate(Game.viewX - Game.viewWidth / 2.0, y, 0.0)

        colorRenderer(model)
    }

    override fun drawVerticalLine(strokeWidth: Double, x: Double) {
        transformationMatrix.setScale(strokeWidth, Game.viewHeight, 0.0)
        transformationMatrix.setTranslate(x, Game.viewY - Game.viewHeight / 2.0, 0.0)

        colorRenderer(model)
    }
}