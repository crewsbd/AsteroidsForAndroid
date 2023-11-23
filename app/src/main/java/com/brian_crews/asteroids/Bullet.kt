package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Bullet(playSpace: GameView, position: Coordinate): GameEntity(playSpace, position) {
    //override val image: BufferedImage = ImageIO.read(File("images/bullet.png"))
    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.bullet)

    override val mass: Float = 1f
    override val radius: Float = 10f
    override var imageScale: Float = (radius*2)/image.width
    override val collidesWithBoundaries: Boolean = false
    var timeToLive:Float = 100f
    init {
        weight = 1f
    }

    override fun update() {
        //print("asteroidupdate")

        facing = (Math.atan((speed.y / speed.x).toDouble()) - (Math.PI/2)).toFloat()
        if(speed.x < 0) {
            facing += (Math.PI).toFloat()
        }
        position.x += speed.x
        position.y += speed.y
        timeToLive -= 1
        if(timeToLive < 0) {
            alive = false
        }

    }
}