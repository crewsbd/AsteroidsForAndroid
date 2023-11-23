package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Player(playSpace: GameView, position: Coordinate):GameEntity(playSpace, position) {

    val titleImage = BitmapFactory.decodeResource(playSpace.resources, R.drawable.title)
    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.title)
    override val mass: Float = 100f
    override val radius: Float = 30f
    override var imageScale: Float = (radius*2)/image.width
    override val collidesWithBoundaries: Boolean = true
    val fireDelay: Float = 45f
    var fireCoolDown: Float = 0f
    val bulletSpeed: Float = 14f


    override fun update() {
        position.x += speed.x
        position.y += speed.y
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