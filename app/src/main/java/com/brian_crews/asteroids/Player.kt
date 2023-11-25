package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Player(playSpace: GameView, position: Coordinate):GameEntity(playSpace, position) {

    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.player)
    override val mass: Float = 100f
    override val screenRadius: Float = .05f //percent of view height
    override val radius: Float = screenRadius * playSpace.gameHeight // pixel radius
    override var imageScale: Float = (radius * 2) / image.width
    override val collidesWithBoundaries: Boolean = true
    val fireDelay: Float = 45f
    var fireCoolDown: Float = 0f
    val bulletSpeed: Float = 14f


    override fun update(deltaTime:Double) {
        position.x += (speed.x*deltaTime).toFloat()
        position.y += (speed.y*deltaTime).toFloat()
        fireCoolDown -= 1;

    }
    fun fire() {
        if(fireCoolDown < 0) {
            fireCoolDown = fireDelay
            val fireDirection = Coordinate(Math.cos(facing.toDouble()).toFloat(), Math.sin(facing.toDouble()).toFloat())
            val bullet: Bullet = Bullet(playSpace, Coordinate(position.x + (fireDirection.x * radius), position.y + (fireDirection.y * radius)))  // Push it out to stop collision


            bullet.speed =
                Coordinate(fireDirection.x * bulletSpeed, fireDirection.y * bulletSpeed)
            playSpace.entities.add(bullet)
        }
    }


}