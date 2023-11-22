import com.brian_crews.asteroids.Coordinate

//import java.awt.Color
//import java.awt.image.BufferedImage
//import java.awt.image.ImageObserver
//import java.awt.Graphics
//import java.awt.Graphics2D

abstract class GameEntity(var playSpace: PlaySpace, var position: Coordinate) {
    abstract val image: BufferedImage
    abstract val mass: Float
    abstract val radius: Float
    abstract var imageScale: Float
    abstract val collidesWithBoundaries: Boolean
    var facing: Float = 0f
    var speed:Coordinate = Coordinate(0f,0f)
    var weight:Float = 1f
    var health: Float = 100f
    var extraDamage: Float = 0f
    var alive: Boolean = true



    abstract fun update()
    fun draw(graphics: Graphics, observer: ImageObserver) {
        //print("Draw entity ${position.x.toString()} ${position.y.toString()}")
        val graphics2d:Graphics2D = graphics as Graphics2D

        val originalTransform = graphics2d.transform // This needs to be put back after draw
        graphics2d.translate((position.x - radius).toInt(), (position.y - radius).toInt())
        graphics2d.scale(imageScale.toDouble(), imageScale.toDouble())
        graphics2d.rotate(facing.toDouble()+(Math.PI/2), (image.width/2).toDouble(), (image.height/2).toDouble()) //Rotate it


        graphics.setColor(Color.WHITE)
        //graphics2d.drawOval(0,0,(radius*2/imageScale).toInt(),(radius*2/imageScale).toInt())

        graphics2d.drawImage(image,0,0,observer)

        graphics2d.transform = originalTransform // Reset this for subsequent image draws
        //graphics2d.dispose()
    }
    fun thrust(amount: Float) {
        speed.x += (Math.cos(facing.toDouble())*amount).toFloat()
        speed.y += (Math.sin(facing.toDouble())*amount).toFloat()

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