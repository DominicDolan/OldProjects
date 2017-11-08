package demo

import display.Game
import loader.loadTexture
import renderer.Painter
import state.LoadState

/**
 * Created by domin on 28/10/2017.
 */
class Assets : LoadState() {

    companion object {
        var circle: Int = 0
            private set
        var colors: Int = 0
            private set
    }

    override fun preLoad() {
        colors = loadTexture("images/colors")
    }

    override fun renderLoadScreen(g: Painter) {
        g.drawCenteredScreenImage(colors, 0.0, 0.0, Game.viewWidth, Game.viewHeight)
    }

    override fun load() {
        circle = loadTexture("images/circle")
    }
}