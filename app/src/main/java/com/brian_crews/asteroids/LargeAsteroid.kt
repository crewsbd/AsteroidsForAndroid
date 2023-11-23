package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory


class LargeAsteroid(playSpace: GameView, position: Coordinate): GameEntity(playSpace, position) {

    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.large_asteroid)
    override val mass: Float = 200f
    override val radius: Float = 60f
    override var imageScale: Float = (radius*2)/image.width
    override val collidesWithBoundaries: Boolean = false
    var rotationSpeed = 0f
    init {
        weight = 2f
    }

    override fun update() {
        //print("asteroidupdate")
        position.x += speed.x
        position.y += speed.y
        facing = (facing + rotationSpeed + Math.PI*2).mod(Math.PI*2).toFloat()
    }
}