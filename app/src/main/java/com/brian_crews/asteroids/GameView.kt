package com.brian_crews.asteroids

import GameEntity
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.media.Image
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.SurfaceHolder
import androidx.core.content.res.ResourcesCompat

/**
 * GameView is our playground.
 */

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread

    //PORTED----
    val FRAME_RATE: Int = 60
    val FRAME_DELAY: Int = 1000/FRAME_RATE
    val STARTING_ASTEROID_DELAY = 90f
    val GAME_OVER_PAUSE: Int = 180 //Time to pause after game over
    //val BIG_FONT =  Font("San-Serif", Font.BOLD, 40)
    //val SMALL_FONT =  Font("San-Serif", Font.BOLD, 16)

    val gameHeight: Int = 480
    val gameWidth: Int = 640
    var newAsteroidDelay: Float = 90f //frames
    var asteroidCountdown = newAsteroidDelay
    var stateCountdown: Int = GAME_OVER_PAUSE

    //val timer:Timer = Timer(FRAME_DELAY, this)
    val entities: MutableList<GameEntity> = mutableListOf()
    var score: Int = 0

    var gameState = GameState.START

    //val titleImage = ImageIO.read(File("images/title.png"))
    //val starField = ImageIO.read(File("images/star_field.png"))

    val titleImage = BitmapFactory.decodeResource(this.resources, R.drawable.title)
    val startField = BitmapFactory.decodeResource(this.resources, R.drawable.star_field)

    var player = Player(this, Coordinate(20f, 20f))

    val inputState: InputManager = InputManager()
    //END PORTED----


    init {

        //PORTED----
        player.speed = (Coordinate(.2f, .1f))
        entities.add(player)
        val asteroid = LargeAsteroid(this, Coordinate(300f,300f))
        asteroid.speed = (Coordinate(-.2f,-.14f))
        entities.add(asteroid)

        setPreferredSize(Dimension(gameWidth, gameHeight))
        setBackground(Color(0,10,30))
        println("Start timer")
        timer.start();
        //END PORTED----

        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)
    }


    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        // start the game thread
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {

    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update() {

        logic()
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        //PORTED


        //These don't have android equivalents that I know of
        //val graphics2d: Graphics2D = g as Graphics2D
        //graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        //graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        //graphics2d.drawImage(starField, 0, 0, this)
        canvas.drawBitmap(starField, 0f, 0f, null) //Last parameter is the paint style. Will need

        for( entity in entities) {
            //entity.draw(g, this)
            entity.draw(canvas, this)  //TODO Convert entities to use canvas
        }

        // Game over state rendering
        if(gameState == GameState.GAME_OVER) {
            //val font = Font("San-Serif", Font.BOLD, 40)


            val fontPaint = Paint()
            fontPaint.typeface = ResourcesCompat.getFont(this.context, R.font.spaceage)  //TODO Get a type face in assets
            fontPaint.textAlign = Paint.Align.CENTER



            //val fontMetrics = graphics2d.getFontMetrics(font)
            //val fontX = gameWidth/2 - fontMetrics.stringWidth("GAME OVER")/2
            // If enough time elapsed, draw reset message
            if(stateCountdown <= 0) {
                //drawStringCentered("Press Fire", gameWidth/2, gameHeight/2+100, SMALL_FONT, graphics2d)
                canvas.drawText("PRESS FIRE", (gameWidth/2).toFloat(),
                    (gameHeight/2+100).toFloat(), fontPaint)


                //graphics2d.drawString("Press Fire", 300, gameHeight/2 + 100)
            }

            //graphics2d.font = font

            fontPaint.typeface = ResourcesCompat.getFont(this.context, R.font.spaceage)  //TODO Get a type face in assets
            fontPaint.textAlign = Paint.Align.CENTER
            //graphics2d.drawString("GAME OVER", fontX, gameHeight/2)
            //drawStringCentered("GAME OVER", gameWidth/2, gameHeight/2, BIG_FONT, graphics2d)
            canvas.drawText("GAME OVER", (gameWidth/2).toFloat(), (gameHeight/2).toFloat(), fontPaint)


        }
        if(gameState == GameState.START) {


            graphics2d.drawImage(titleImage, 0, 0, this)
            canvas.drawBitmap()

        }
        //Debug message
        graphics2d.font = SMALL_FONT
        graphics2d.drawString("SCORE: ${score}", 10, 20)
        //END PORTED

    }

    private fun logic() {  //TODO Update this to use android stuff
        // Handle input
        if(gameState == GameState.PLAYING) {
            if (inputState.forwardKey) {
                player.thrust(.1f)
            }
            if (inputState.backwardKey) {
                player.thrust(-.1f)
            }
            if (inputState.rightKey) {
                player.facing = (player.facing + 2 * Math.PI + .1).toFloat().mod(2f * Math.PI.toFloat())
            }
            if (inputState.leftKey) {
                player.facing = (player.facing + 2 * Math.PI - .1).toFloat().mod(2f * Math.PI.toFloat())
            }
            if (inputState.fireKey) {
                player.fire()
            }
        }
        else if(gameState == GameState.START) {
            if(inputState.fireKey) {
                gameState = GameState.PLAYING
            }
        } else if(gameState == GameState.GAME_OVER) {
            if(stateCountdown <= 0) {
                if(inputState.fireKey) {
                    resetGame()
                    gameState = GameState.PLAYING
                }
            }
            else {
                stateCountdown--
                println("Countdown $stateCountdown")
            }

        }

        // New asteroids
        asteroidCountdown -= 1
        newAsteroidDelay -= .01f
        if(asteroidCountdown < 0) {
            print("New asteroid")
            var newX = 0
            var newY = 0
            var newSpeedX = 0
            var newSpeedY = 0
            when(Random().nextInt(4)) {
                0 -> {
                    println(" from Right")
                    newX = gameWidth + 60;
                    newY = Random().nextInt(gameHeight)
                    newSpeedX = -1
                    newSpeedY = 0
                }
                1 -> {
                    println(" from Left")
                    newX = -60
                    newY = Random().nextInt(gameHeight)
                    newSpeedX = 1
                    newSpeedY = 0
                }
                2 -> {
                    println(" from Bottom")
                    newY = gameHeight + 60
                    newX = Random().nextInt(gameWidth)
                    newSpeedX = 0
                    newSpeedY = -1
                }
                3 -> {
                    println(" from Top")
                    newY = -60
                    newX = Random().nextInt(gameWidth)
                    newSpeedX = 0
                    newSpeedY = 1
                }
            }
            asteroidCountdown = newAsteroidDelay
            val newAsteroid = LargeAsteroid(this, Coordinate(newX.toFloat(), newY.toFloat()))
            newAsteroid.speed = Coordinate(newSpeedX.toFloat(), newSpeedY.toFloat())
            newAsteroid.rotationSpeed = Random().nextFloat(.1f) - .05f
            entities.add(newAsteroid)
        }


        // Boundary collision detection
        for (entity in entities) {
            if(gameState != GameState.START) { // Only move once the game is going
                entity.update()
            }
            // Entity logic
            if(entity.collidesWithBoundaries) {
                if(entity.position.x > gameWidth) {
                    entity.position.x = gameWidth.toFloat()
                    entity.speed.x = 0f
                }
                else if(entity.position.x < 0) {
                    entity.position.x = 0f
                    entity.speed.x = 0f
                }
                if(entity.position.y > gameHeight) {
                    entity.position.y = gameHeight.toFloat()
                    entity.speed.y = 0f
                }
                else if(entity.position.y < 0) {
                    entity.position.y = 0f
                    entity.speed.y = 0f
                }
            }
        }

        // Check collisions

        var outter = 0
        while(outter < entities.size) {
            var inner = outter + 1 // Don't need to repeat here
            while(inner < entities.size) {
                if(entities[outter].collidesWith(entities[inner]) ) {
                    println("$inner vs $outter")
                    entities[outter].manageCollision(entities[inner])
                }
                inner++
            }
            outter++
        }

        // Remove dead entities
        val entityIterator = entities.iterator()
        while(entityIterator.hasNext()) {
            val entity = entityIterator.next()
            if (!entity.alive) {
                entityIterator.remove()
            }
        }
        if(!player.alive && gameState != GameState.GAME_OVER) {
            gameState = GameState.GAME_OVER
            stateCountdown = GAME_OVER_PAUSE
        }

    }
    enum class GameState {
        START,
        PLAYING,
        GAME_OVER
    }
    fun resetGame() {
        score = 0
        newAsteroidDelay = STARTING_ASTEROID_DELAY
        player = Player(this, Coordinate(gameWidth.toFloat()/2, gameHeight.toFloat()/2))
        entities.add(player)
    }
    fun drawStringCentered(string:String, x:Int, y:Int, font:Font, graphics2d: Graphics2D) {
        val fontMetrics = graphics2d.getFontMetrics(font)
        val fontX = -fontMetrics.stringWidth(string)/2 + x
        graphics2d.font = font
        graphics2d.drawString(string, fontX, y)
    }

}