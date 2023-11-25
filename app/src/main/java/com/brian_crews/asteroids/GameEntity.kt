package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

abstract class GameEntity(var playSpace: GameView, var position: Coordinate) {
    abstract val image: Bitmap
    abstract val mass: Float
    abstract val screenRadius: Float // Percent of view height
    abstract val radius: Float
    abstract var imageScale: Float
    abstract val collidesWithBoundaries: Boolean
    var facing: Float = 0f
    var speed:Coordinate = Coordinate(0f,0f)
    var weight:Float = 1f
    var health: Float = 100f
    var extraDamage: Float = 0f
    var alive: Boolean = true



    abstract fun update(deltaTime:Double)
    fun draw(graphics: Canvas, observer: GameView) {
        //print("Draw entity ${position.x.toString()} ${position.y.toString()}")

        var transform:Matrix = Matrix()



        transform.postScale(imageScale, imageScale)
        transform.postRotate(Math.toDegrees(facing.toDouble()).toFloat() + 90, radius, radius)
        transform.postTranslate(position.x - radius, position.y - radius)
        graphics.drawBitmap(image, transform,null )

    }
    fun thrust(amount: Float, deltaTime:Double) {
        speed.x += (Math.cos(facing.toDouble())*amount*deltaTime).toFloat()
        speed.y += (Math.sin(facing.toDouble())*amount*deltaTime).toFloat()

    }
    fun collidesWith(entity: GameEntity): Boolean {
        val xDistance = this.position.x - entity.position.x
        val yDistance = this.position.y - entity.position.y

        val distance = Math.sqrt( Math.pow(xDistance.toDouble(), 2.0) +  Math.pow(yDistance.toDouble(), 2.0) )
        if(distance < this.radius + entity.radius) {
            //println("Collision!")
            return true
        }
        else {
            return false
        }
    }
    fun manageCollision(entity: GameEntity) {
        this.alive = false
        entity.alive = false
        if(this is Bullet || entity is Bullet) {
            playSpace.score++
        }
    }
}