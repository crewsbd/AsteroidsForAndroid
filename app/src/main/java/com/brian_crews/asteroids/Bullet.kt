package com.brian_crews.asteroids
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Bullet(playSpace: PlaySpace, position: Coordinate): Entity(playSpace, position) {
    override val image: BufferedImage = ImageIO.read(File("images/bullet.png"))
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