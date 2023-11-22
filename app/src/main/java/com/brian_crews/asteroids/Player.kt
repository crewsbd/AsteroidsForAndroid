package com.brian_crews.asteroids
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

class Player(playSpace: PlaySpace, position: Coordinate):Entity(playSpace, position) {
    override val image: BufferedImage = ImageIO.read(File("images/player.png"))
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