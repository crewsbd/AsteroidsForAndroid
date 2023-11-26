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
    val deadSpot = 40f //pixels for now

    var movementPointerActive: Boolean = false
    var movementPointerID:Int = 0
    var movementTouchOrigin: Coordinate = Coordinate(0f,0f)


    fun update(event: MotionEvent) {  // Called when motion event occurs
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
            MotionEvent.ACTION_UP -> {  //Last touch up. Clear all input
                fireKey = false
                leftKey = false
                rightKey = false
                forwardKey = false
                backwardsKey = false
                boostKey = false
                movementPointerID = 0
                movementPointerActive = false
            }
            MotionEvent.ACTION_POINTER_UP -> {  // A finger has been raised
                if(movementPointerActive && movementPointerID == event.actionIndex) {  // The movement pointer was released
                    movementPointerID = 0
                    movementPointerActive = false
                }
                else {  // fire button PROBABLY released.  I'm tired.
                    fireKey = false
                }

            }
            MotionEvent.ACTION_MOVE -> {
                if(event.actionIndex == movementPointerID && movementPointerActive) { // Dealting with the movement touch
                    if(event.y < movementTouchOrigin.y - deadSpot) {
                        forwardKey = true
                    }
                    else {
                        forwardKey = false
                    }
                    if(event.x > movementTouchOrigin.x + deadSpot) {
                        rightKey = true

                    }
                    else {
                        rightKey = false
                    }
                    if(event.x < movementTouchOrigin.x - deadSpot) {
                        leftKey = true
                    }
                    else {
                        leftKey = false
                    }



                }

            }
        }
    }

}