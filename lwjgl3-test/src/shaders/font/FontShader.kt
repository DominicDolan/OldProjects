package shaders.font

import matrices.ProjectionMatrix
import matrices.ViewMatrix
import renderer.ShaderProgram

class FontShader : ShaderProgram("font/fontVertex.glsl", "font/fontFragment.glsl") {
    private val ratio = 1f

    var position: Int = 0
    var textureCoords: Int = 0
    var translation: Int = 0
    var projectionAndView: Int = 0
    var scale: Int = 0
    var color: Int = 0

    init {
        loadRatio()
    }

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "textureCoords")
    }

    override fun getAllUniformLocations() {
        translation = getUniformLocation("translation")
        color = getUniformLocation("color")
        scale = getUniformLocation("scale")
        projectionAndView = getUniformLocation("projectionAndView")
    }


    fun loadProjectionAndView(projectionMatrix: ProjectionMatrix, viewMatrix: ViewMatrix) {
        loadMatrix(this.projectionAndView, viewMatrix.createWithProjection(projectionMatrix))
    }

    fun loadTranslation(x: Float, y: Float) {
        loadVector(translation, x, y)
    }

    private fun loadRatio() {
        loadFloat(scale, ratio)
    }

}
