package com.brian_crews.asteroids

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

abstract class GameEntity(var playSpace: GameView, var position: Coordinate) {
    abstract val image: Bitmap
    abstract val mass: Float
    abstract val screenRadius: Float // Percent of view height
    abstract var radius: Float
    abstract var imageScale: Float
    abstract val collidesWithBoundaries: Boolean
    var facing: Float = 0f
    var speed:Coordinate = Coordinate(0f,0f)
    var weight:Float = 1f
    var health: Float = 100f
    var extraDamage: Float = 0f
    var alive: Boolean = true



    abstract fun update(deltaTime:Double) // Updates the entity location, orientation etc.
    abstract fun deathAction()  // This action gets performed when an entity dies.  Might spawn a new entity, or make an expolosion
    fun draw(graphics: Canvas, observer: GameView) {  // Draws the entity on the screen
        //print("Draw entity ${position.x.toString()} ${position.y.toString()}")

        var transform:Matrix = Matrix()



        transform.postScale(imageScale, imageScale)
        transform.postRotate(Math.toDegrees(facing.toDouble()).toFloat() + 90, radius, radius)
        transform.postTranslate(position.x - radius, position.y - radius)
        graphics.drawBitmap(image, transform,null )

    }
    fun thrust(amount: Float, deltaTime:Double) {  //Applies thrust to the entity
        speed.x += (Math.cos(facing.toDouble())*amount*deltaTime).toFloat()
        speed.y += (Math.sin(facing.toDouble())*amount*deltaTime).toFloat()

    }
    fun collidesWith(entity: GameEntity): Boolean {  //CHecks if there is a collision
        val xDistance = this.position.x - entity.position.x
        val yDistance = this.position.y - entity.position.y

        val distance = Math.sqrt( Math.pow(xDistance.toDouble(), 2.0) +  Math.pow(yDistance.toDouble(), 2.0) )
        if(this.alive && entity.alive && distance < this.radius + entity.radius) {
            return true
        }
        else {
            return false
        }
    }
    fun manageCollision(entity: GameEntity) {  // If a collision has occured, call this. It kills the entity
        this.alive = false
        entity.alive = false
        if(this is Bullet || entity is Bullet) {
            playSpace.score++
        }
    }
    fun updateSize() {
        radius = screenRadius * playSpace.gameHeight // pixel radius
        imageScale = (radius * 2) / image.width
    }

}