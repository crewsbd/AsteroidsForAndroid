package com.brian_crews.asteroids

import android.util.Log
import android.view.MotionEvent

class InputManager {
    var forwardKey: Boolean = false
    var backwardsKey: Boolean = false
    var leftKey: Boolean = false
    var rightKey: Boolean = false
    var boostKey: Boolean = false
    var fireKey: Boolean = false
    var mousePosition: Coordinate = Coordinate(0f,0f)

    var directionActive: Boolean = false

    var touches: HashMap<Int, Touch> = HashMap<Int, Touch>()

    fun update(event: MotionEvent) {  // Called when motion event occurs
        val id = event.getPointerId(0)

        Log.d("INPUT", "MotionEvent")
        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> { // First touch down
                Log.d("INPUT", "Action down ${id}")
                if (touches[id] == null) {  // That touch doesn't exist yet
                    if(event.x < 500) {  // Motion
                        Log.d("INPUT", "Motion press")
                        touches[id] = Touch(event.x, event.y, TouchType.MOTION)
                        directionActive = true
                    }
                    else {  // Fire
                        Log.d("INPUT", "Fire press")
                        touches[id] = Touch(event.x, event.y, TouchType.FIRE)
                    }
                }
            }
            MotionEvent.ACTION_POINTER_DOWN -> { // Subsequent touch

            }
            MotionEvent.ACTION_UP -> {  //Last touch up. Clear all input
                fireKey = false
                leftKey = false
                rightKey = false
                forwardKey = false
                backwardsKey = false
                boostKey = false
                touches.clear() // Just to be sure
            }
            MotionEvent.ACTION_POINTER_UP -> {  // A finger has been raised

            }
            MotionEvent.ACTION_MOVE -> {

            }
        }
        // Analyze touch events
        touches.forEach { touch ->
            Log.d("INPUT", "Found a touch instance")
            if(touch.value.Type == TouchType.FIRE) { //Found a fire key pressed
                fireKey = true
            }
            if(touch.value.Type == TouchType.MOTION) { //Found a motion key pressed
                directionActive = true

                //TEMPORARY TEST CODE
                forwardKey = true
                leftKey = true
            }
        }
    }

    class Touch(var x:Float, var y:Float, val Type: TouchType) {

    }
    enum class  TouchType {
        FIRE,
        MOTION
    }
}