package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Bullet(playSpace: GameView, position: Coordinate): GameEntity(playSpace, position) {
    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.bullet)

    override val mass: Float = 1f
    override val screenRadius: Float = .01f
    override var radius: Float = screenRadius * playSpace.gameHeight
    override var imageScale: Float = (radius*2)/image.width
    override val collidesWithBoundaries: Boolean = false
    var timeToLive:Float = 10f //seconds
    init {

    }

    override fun update(deltaTime: Double) {  // Update bullet location and time to live counter.

        facing = (Math.atan((speed.y / speed.x).toDouble()) - (Math.PI/2)).toFloat()  //Always faces direction of motion
        if(speed.x < 0) { // Account for atan
            facing += (Math.PI).toFloat()
        }
        position.x += (speed.x * deltaTime).toFloat()
        position.y += (speed.y * deltaTime).toFloat()
        timeToLive -= deltaTime.toFloat()
        if(timeToLive < 0) {
            alive = false
        }
    }
    override fun deathAction() {  // Nothing yet
        if(!alive) {
            //Do stuff
        }
    }
}