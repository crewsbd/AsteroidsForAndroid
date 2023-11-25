package com.brian_crews.asteroids
import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {
    private var running: Boolean = false

    private val targetFPS = 60 // frames per second, the rate at which you would like to refresh the Canvas

    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    override fun run() {  // Starts the thread
        var startTime: Long = System.nanoTime()
        var deltaTime: Long  // Used to calculate motion in update calls
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong() // Time for one frame

        while (running) {  // Game loop
            deltaTime = System.nanoTime() - startTime
            startTime = System.nanoTime()
            canvas = null

            try {
                // locking the canvas allows us to draw on to it
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    this.gameView.update(deltaTime)
                    this.gameView.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = if((targetTime - timeMillis) > 0) (targetTime - timeMillis) else 0 // don't sleep negative

            try {
                sleep(waitTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var canvas: Canvas? = null
    }

}