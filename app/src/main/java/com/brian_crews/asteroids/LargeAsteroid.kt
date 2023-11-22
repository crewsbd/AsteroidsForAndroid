package com.brian_crews.asteroids

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class LargeAsteroid(playSpace: PlaySpace, position: Coordinate): Entity(playSpace, position) {
    override val image: BufferedImage = ImageIO.read(File("images/large_asteroid.png"))
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