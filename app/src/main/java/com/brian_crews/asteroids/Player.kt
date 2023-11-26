package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Player(playSpace: GameView, position: Coordinate):GameEntity(playSpace, position) {

    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.player)
    override val mass: Float = 100f
    override val screenRadius: Float = .05f //percent of view height
    override var radius: Float = screenRadius * playSpace.gameHeight // pixel radius
    override var imageScale: Float = (radius * 2) / image.width
    override val collidesWithBoundaries: Boolean = true
    val fireDelay: Float = 45f
    var fireCoolDown: Float = 0f
    val bulletSpeed: Float = 1000f //pixels per second


    override fun update(deltaTime:Double) { // Updates player location and the fire cooldown counter
        position.x += (speed.x*deltaTime).toFloat()
        position.y += (speed.y*deltaTime).toFloat()
        fireCoolDown -= 1;

    }
    override fun deathAction() {  // Nothing happens on death
        if(!alive) {
            //Do stuff
        }
    }
    fun fire() {  //Creates a new bullet heading in the right direction
        if(fireCoolDown < 0) {
            fireCoolDown = fireDelay
            val fireDirection = Coordinate(Math.cos(facing.toDouble()).toFloat(), Math.sin(facing.toDouble()).toFloat())
            val bullet: Bullet = Bullet(playSpace, Coordinate(position.x + (fireDirection.x * radius*1.5f), position.y + (fireDirection.y * radius * 1.5f)))  // Push it out to stop collision


            bullet.speed =
                Coordinate(fireDirection.x * bulletSpeed + speed.x, fireDirection.y * bulletSpeed + speed.y)
            playSpace.entities.add(bullet)
        }
    }


}