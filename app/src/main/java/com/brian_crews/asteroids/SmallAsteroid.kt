package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory


class SmallAsteroid(playSpace: GameView, position: Coordinate): GameEntity(playSpace, position) {

    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.large_asteroid)
    override val mass: Float = 200f
    override val screenRadius: Float = .07f  // Entity sizes are in units of screen height
    override var radius: Float = screenRadius * playSpace.gameHeight


    override var imageScale: Float = (radius*2)/image.width
    override val collidesWithBoundaries: Boolean = false
    var rotationSpeed = .2f
    init {

    }

    override fun update(deltaTime: Double) { // Updates the location on spins the asteroid
        //print("asteroidupdate")
        position.x += (speed.x * deltaTime).toFloat()
        position.y += (speed.y * deltaTime).toFloat()
        facing = (facing + (rotationSpeed * deltaTime) + Math.PI*2).mod(Math.PI*2).toFloat()
    }
    override fun deathAction() {
        if(!alive) {
            //Do stuff
        }
    }
}