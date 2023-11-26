package com.brian_crews.asteroids

import android.util.Log
import android.view.MotionEvent

class InputManager {
    var forwardKey: Boolean = false  // These are what the game uses to control the player
    var backwardsKey: Boolean = false
    var leftKey: Boolean = false
    var rightKey: Boolean = false
    var boostKey: Boolean = false
    var fireKey: Boolean = false
    var mousePosition: Coordinate = Coordinate(0f,0f)

    val deadSpot = 40f //pixels for now. How far you need to move left finger to register motion key

    var movementPointerActive: Boolean = false  // Is the motion pointer actvive(pointer on left side of screen)
    var movementPointerID:Int = 0  // Which pointer is the motion pointer
    var movementTouchOrigin: Coordinate = Coordinate(0f,0f)  // Where did the motion pointer touch first. Used to determine distance from current location and trigger keys


    fun update(event: MotionEvent) {  // Called when motion events occurs
        val id = event.getPointerId(0)

        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> { //Any pointer down

                val index = event.actionIndex

                if(event.x < 600) {  // Motion touch
                    if(!movementPointerActive) {
                        movementPointerID =
                            index  // Store the index of the pointer that will be the movement controller
                        movementPointerActive = true
                        movementTouchOrigin = Coordinate(event.x, event.y)
                    } //Else ignore the touch
                }
                else { // Fire touch
                    fireKey = true
                }
            }
            MotionEvent.ACTION_UP -> {  // Last touch up. Clear all input since we know nothing is pressed
                fireKey = false
                leftKey = false
                rightKey = false
                forwardKey = false
                backwardsKey = false
                boostKey = false
                movementPointerID = 0
                movementPointerActive = false
            }
            MotionEvent.ACTION_POINTER_UP -> {  // A finger has been raised, not necessarily all though.
                if(movementPointerActive && movementPointerID == event.actionIndex) {  // The movement pointer was released
                    movementPointerID = 0
                    movementPointerActive = false
                }
                else {  // fire button PROBABLY released.  I'm tired. I'll make this better later
                    fireKey = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(event.actionIndex == movementPointerID && movementPointerActive) { // Dealing with the movement touch
                    forwardKey = event.y < movementTouchOrigin.y - deadSpot  // Check if movement triggeres
                    rightKey = event.x > movementTouchOrigin.x + deadSpot
                    leftKey = event.x < movementTouchOrigin.x - deadSpot
                }
            }
        }
    }

}