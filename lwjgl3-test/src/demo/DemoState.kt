package demo

import display.Game
import loader.WorldFactory
import org.jbox2d.common.Vec2
import renderer.Painter
import units.x12th
import units.y12th
import world.WorldGraphics
import world.WorldSection
import state.State

/**
 * Created by domin on 08/11/2017.
 */
class DemoState : State() {
    var position = 0.0
    lateinit var world: WorldSection

    override fun init() {
        val wf = WorldFactory("world1", position = Vec2(-27f, -17f), scale = 0.01)
        world = wf.world
    }

    override fun update(delta: Float) {

    }

    override fun render(g: Painter) {
        g.color = 0x40FF40FF
        g.drawCenteredRect(Game.viewX, Game.viewY, Game.viewWidth - 0.5, Game.viewHeight - 0.5)
        g.color = 0xFF4040FF
        g.drawRect(2.0, 1.0, 1.0, 1.0)
        drawGrid(g)

        for (graphic in world.graphics){
            if (graphic is WorldGraphics.MatrixModel){
                g.drawImage(graphic.texture, graphic.matrix)
            } else {
                g.fillPolygon(graphic)
            }
        }

        g.color = 0xFF4040FF
        g.drawRotatedRect(-6.0, -2.0, 3.0, 1.0, 5.0)
        g.drawCenteredImage(Assets.circle, 0.0, 0.0, 1.0, 1.0)

        g.drawLine(0.02, 0.0, 0.0, 2.0, 4.0)

        g.color = 0x787878FF
        g.drawPositionalRect(-4.0 + position, 4.0, -2.5 + position, 3.0)

        g.drawCenteredImage(Assets.circle, -3.0, 0.0, 1.0, 1.0)

//                    g.drawScreenRect(0.0, 0.0, 3.0, 3.0)


        g.color = 0xFFF540FF
        g.drawText("Hello, World", 8.0, -5.5* x12th, -5* y12th)

        g.fillRotatedEllipse(6.0, -2.0, 4.0, 2.0, position*25.0)
//                    circleRenderer()
        position += 0.01
    }

    private fun drawGrid(g: Painter) {
        g.color = 0xFFFFFFFF
        val strokeWidth = 0.01
        var count = Math.floor(Game.width/2.0).toInt()*5
        for (i in -count..count){
            g.drawVerticalLine(strokeWidth, (Game.viewX + i))
        }

        count = Math.floor(Game.height/2.0).toInt()*5
        for (i in -count..count){
            g.drawHorizontalLine(strokeWidth, (Game.viewY + i))
        }

    }

}