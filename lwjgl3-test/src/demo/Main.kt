package demo

import display.Game
import display.GameBuilder

/**
 * Created by domin on 26/10/2017.
 */

fun main(args: Array<String>) {

    GameBuilder()
            .setResolution(1280, 720)
            .setViewPort(height = 10.0)
            .setLoader(Assets())
            .setStartingState(DemoState())
            .start()

    Game.update()

    Game.destroy()
}


