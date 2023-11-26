// This contains most of the game logic and is the surface that displays the game.

package com.brian_crews.asteroids

import android.content.Context
import android.content.Entity
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.Image
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.SurfaceHolder
import androidx.core.content.res.ResourcesCompat
import android.content.res.Resources
import android.media.metrics.PlaybackErrorEvent
import android.provider.Settings.Global.getString
import android.util.Log
import java.util.Random

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread

    val DEBUG = false
    //PORTED----
    val STARTING_ASTEROID_DELAY = 90f
    val GAME_OVER_PAUSE: Int = 80 //Time to pause after game over
    //val BIG_FONT =  Font("San-Serif", Font.BOLD, 40)
    //val SMALL_FONT =  Font("San-Serif", Font.BOLD, 16)


    //var gameHeight:Int = this.height
    //var gameWidth:Int = this.width
    var gameWidth: Int = 640
    var gameHeight: Int = 480

    var newAsteroidDelay: Float = 90f //frames
    var asteroidCountdown = newAsteroidDelay
    var stateCountdown: Int = GAME_OVER_PAUSE

    //val timer:Timer = Timer(FRAME_DELAY, this)
    val entities: MutableList<GameEntity> = mutableListOf()
    var score: Int = 0
    var highScores: MutableList<Int> = mutableListOf()
    var deltaTime: Double = 0.0 // So we can print it

    var gameState = GameState.START // Start the game in START mode. It will display a title and wait for a key press


    //val titleImage = ImageIO.read(File("images/title.png"))
    //val starField = ImageIO.read(File("images/star_field.png"))

    val titleImage = BitmapFactory.decodeResource(this.resources, R.drawable.title)
    val starField = BitmapFactory.decodeResource(this.resources, R.drawable.star_field)

    var player:Player = Player(this, Coordinate(1f,1f))

    val inputState: InputManager = InputManager()
    //END PORTED----


    init {
        // add callback
        holder.addCallback(this)

        val sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        //val defaultValue = resources.getInteger("prefs")
        highScores.add(sharedPref.getInt("HighScore1", 0))
        highScores.add(sharedPref.getInt("HighScore2", 0))
        highScores.add(sharedPref.getInt("HighScore3", 0))

        // instantiate the game thread
        thread = GameThread(holder, this)
    }


    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        // start the game thread
        player = Player(this, Coordinate(20f, 20f))

        entities.add(player)
        entities.add(LargeAsteroid(this, Coordinate(300f, 300f)))

        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {

        gameWidth = width
        gameHeight = height
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
    fun update(deltaTime:Long) {
        logic(deltaTime)
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
            fontPaint.typeface = ResourcesCompat.getFont(this.context, R.font.spaceage)  // Set the font for game over screen
            fontPaint.textAlign = Paint.Align.CENTER
            fontPaint.textSize = gameHeight / 10f
            fontPaint.color = Color.RED

            canvas.drawText("GAME OVER", (gameWidth/2).toFloat(),
                (gameHeight/2).toFloat(), fontPaint)
            canvas.drawText("#1 ${highScores[0]}", (gameWidth/2).toFloat(),
                (gameHeight/2).toFloat()+200f, fontPaint)
            canvas.drawText("#2 ${highScores[1]}", (gameWidth/2).toFloat(),
                (gameHeight/2).toFloat()+250f, fontPaint)
            canvas.drawText("#3 ${highScores[2]}", (gameWidth/2).toFloat(),
                (gameHeight/2).toFloat() + 300f, fontPaint)


            if(stateCountdown <= 0) {
                canvas.drawText("PRESS FIRE", (gameWidth/2).toFloat(),
                    (gameHeight/2+100).toFloat(), fontPaint)
            }

            fontPaint.typeface = ResourcesCompat.getFont(this.context, R.font.spaceage)  //TODO Get a type face in assets
            fontPaint.textAlign = Paint.Align.CENTER
            canvas.drawText("GAME OVER", (gameWidth/2).toFloat(), (gameHeight/2).toFloat(), fontPaint)


        }
        if(gameState == GameState.START) {


            //graphics2d.drawImage(titleImage, 0, 0, this)
            canvas.drawBitmap(titleImage,0f,0f,null)  // Draw the title until a button is pressed

        }

        //Some debug info
        if (DEBUG) {
        var boldText: Paint = Paint()
        boldText.textSize = 40f
        boldText.color = Color.CYAN
        canvas.drawText("player (${Math.floor(player.position.x.toDouble())}, ${Math.floor(player.position.y.toDouble())})", 200f, 100f, boldText)
        canvas.drawText("screen (${gameWidth}, ${gameHeight})",200f,150f, boldText )
        canvas.drawText("deltaTime ${"%.10f".format(deltaTime)}", 200f, 200f, boldText) }


    }

    private fun logic(deltaTime: Long) {  //TODO Update this to use android stuff
        // Handle input
        val deltaTimeSeconds: Double = (deltaTime.toDouble()/1000000000) //Elapsed time in seconds
        this.deltaTime = deltaTimeSeconds

        if(gameState == GameState.PLAYING) {
            if (inputState.forwardKey) {
                player.thrust(200f, deltaTimeSeconds )
            }
            if (inputState.backwardsKey) {
                player.thrust(-200f, deltaTimeSeconds)
            }
            if (inputState.rightKey) {
                player.facing = (player.facing + 2 * Math.PI + 4 * deltaTimeSeconds).toFloat().mod(2f * Math.PI.toFloat())
            }
            if (inputState.leftKey) {
                player.facing = (player.facing + 2 * Math.PI - 4 * deltaTimeSeconds).toFloat().mod(2f * Math.PI.toFloat())
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
                    newSpeedX = -10  //Speeds are pixels per second
                    newSpeedY = 0
                }
                1 -> {
                    println(" from Left")
                    newX = -60
                    newY = Random().nextInt(gameHeight)
                    newSpeedX = 10
                    newSpeedY = 0
                }
                2 -> {
                    println(" from Bottom")
                    newY = gameHeight + 60
                    newX = Random().nextInt(gameWidth)
                    newSpeedX = 0
                    newSpeedY = -10 //
                }
                3 -> {
                    println(" from Top")
                    newY = -60
                    newX = Random().nextInt(gameWidth)
                    newSpeedX = 0
                    newSpeedY = 10
                }
            }
            asteroidCountdown = newAsteroidDelay
            val newAsteroid = LargeAsteroid(this, Coordinate(newX.toFloat(), newY.toFloat()))
            newAsteroid.speed = Coordinate(newSpeedX.toFloat(), newSpeedY.toFloat())
            newAsteroid.rotationSpeed = ((Random().nextFloat()*.1f) - .05f) * 20f
            entities.add(newAsteroid)
        }


        // Boundary collision detection
        for (entity in entities) {
            if(gameState != GameState.START) { // Only move once the game is going
                entity.update(deltaTimeSeconds)
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
                    Log.d("COLLISION","${inner} speed:${entities[inner].speed.x},${entities[inner].speed.y} vs ${outter} speed:${entities[outter].speed.x},${entities[outter].speed.y}")
                    entities[outter].manageCollision(entities[inner])
                }
                inner++
            }
            outter++
        }

        // Death actions
        for( entity in entities) {
            if(!entity.alive) {
                entity.deathAction()
            }
        }


        // Remove dead entities
        val entityIterator = entities.iterator()
        while(entityIterator.hasNext()) {
            val entity = entityIterator.next()
            if (!entity.alive) {
                entityIterator.remove()
            }
        }
        // Add queued entities



        if(!player.alive && gameState != GameState.GAME_OVER) {  //Player died
            highScores.add(score)  // These lines store the new top three high scores.
            highScores.sort()
            highScores.reverse()

            val sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putInt("HighScore1", highScores[0])
                putInt("HighScore2", highScores[1])
                putInt("HighScore3", highScores[2])

                apply()
            }
            gameState = GameState.GAME_OVER
            stateCountdown = GAME_OVER_PAUSE
        }

    }
    override fun onTouchEvent(event: MotionEvent): Boolean {  // Just pass this on to the input manager

        //Log.d("INPUT", "onTouchEVent occured")
        inputState.update(event)
        return true
    }

    enum class GameState {  // Tracks the games overall state.
        START,
        PLAYING,
        GAME_OVER
    }
    fun resetGame() {  // Reset the player and score and asteroid spawn speed
        score = 0
        newAsteroidDelay = STARTING_ASTEROID_DELAY
        player = Player(this, Coordinate(gameWidth.toFloat()/2, gameHeight.toFloat()/2))
        entities.add(player)
    }
    fun addAsteroid(location: Coordinate) { // Remove this
        //val newAsteroid = SmallAsteroid(this,  Coordinate(2f, 2f))
        //newAsteroid.speed = Coordinate(0f, 0f)
        //newAsteroid.rotationSpeed = ((Random().nextFloat()*.1f) - .05f) * 20f
        //entities.add(newAsteroid)
    }
    fun queueEntity(newEntity: Entity) {  // Ques an entity to be added to the game. Avoids crashing on for loop

    }

}