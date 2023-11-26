package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.contentValuesOf
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin


class LargeAsteroid(playSpace: GameView, position: Coordinate): GameEntity(playSpace, position) {

    override val image: Bitmap = BitmapFactory.decodeResource(playSpace.resources, R.drawable.large_asteroid)
    override val mass: Float = 200f
    override val screenRadius: Float = .15f // 15% of screen height
    override var radius: Float = screenRadius * playSpace.gameHeight


    override var imageScale: Float = (radius*2)/image.width
    override val collidesWithBoundaries: Boolean = false
    var rotationSpeed = .2f
    init {

    }

    override fun update(deltaTime: Double) { // Updates location and spins the asteroid
        //print("asteroidupdate")
        position.x += (speed.x * deltaTime).toFloat()
        position.y += (speed.y * deltaTime).toFloat()
        facing = (facing + (rotationSpeed * deltaTime) + Math.PI*2).mod(Math.PI*2).toFloat()
    }
    override fun deathAction() {  // This should make a new asteroid.
        //Make first child asteroid
        val newDirection1 = atan(speed.y/speed.x.toDouble()) - PI/2 // Rotate CC 90deg
        val newVectorFacing1X = cos(newDirection1)  // Vector direction unit
        val newVectorFacing1Y = sin(newDirection1)
        val newLocation1X = newVectorFacing1X * 70 + position.x
        val newLocation1Y = newVectorFacing1Y * 70 + position.y
        val newSpeed1X = newVectorFacing1X * 40 + speed.x // New speed
        val newSpeed1Y = newVectorFacing1Y * 40 + speed.y
        val child1 = SmallAsteroid(playSpace, Coordinate(newLocation1X.toFloat(), newLocation1Y.toFloat()))
        child1.speed.x = newSpeed1X.toFloat()
        child1.speed.y = newSpeed1Y.toFloat()

        //Make second child asteroid
        val newDirection2 = atan(speed.y/speed.x.toDouble()) + PI/2 // Rotate CC 90deg
        val newVectorFacing2X = cos(newDirection2)  // Vector direction unit
        val newVectorFacing2Y = sin(newDirection2)
        val newLocation2X = newVectorFacing2X * 70 + position.x
        val newLocation2Y = newVectorFacing2Y * 70 + position.y
        val newSpeed2X = newVectorFacing2X * 40 + speed.x // New speed
        val newSpeed2Y = newVectorFacing2Y * 40 + speed.y
        val child2 = SmallAsteroid(playSpace, Coordinate(newLocation2X.toFloat(), newLocation2Y.toFloat()))
        child2.speed.x = newSpeed2X.toFloat()
        child2.speed.y = newSpeed2Y.toFloat()


        playSpace.queueEntity(child1)
        playSpace.queueEntity(child2)
    }
}