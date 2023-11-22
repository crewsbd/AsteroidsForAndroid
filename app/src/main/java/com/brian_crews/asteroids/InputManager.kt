package com.brian_crews.asteroids
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class InputManager: KeyListener, MouseListener {
    var forwardKey: Boolean = false
    var backwardKey: Boolean = false
    var leftKey: Boolean = false
    var rightKey: Boolean = false
    var boostKey: Boolean = false
    var fireKey: Boolean = false
    var mousePosition: Coordinate = Coordinate(0f,0f)

    override fun keyTyped(e: KeyEvent?) {
    }
    override fun mouseClicked(e: MouseEvent?) {
    }
    override fun mousePressed(e: MouseEvent?) {
        if (e != null) {
            println(" $e.button")
            when(e.button) {
                1 -> fireKey = true
            }
        }
    }

    override fun mouseReleased(e: MouseEvent?) {
        if (e != null) {
            println(" $e.button")
            when(e.button) {
                1 -> fireKey = false
            }
        }
    }

    override fun mouseEntered(e: MouseEvent?) {
    }
    override fun mouseExited(e: MouseEvent?) {
    }
    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {

            when(e.keyCode) {
                KeyEvent.VK_W -> forwardKey = true
                KeyEvent.VK_S -> backwardKey = true
                KeyEvent.VK_A -> leftKey = true
                KeyEvent.VK_D -> rightKey = true
                KeyEvent.VK_SHIFT -> boostKey = true
                KeyEvent.VK_SPACE -> fireKey = true
            }
        }
    }
    override fun keyReleased(e: KeyEvent?) {
        if (e != null) {

            when(e.keyCode) {
                KeyEvent.VK_W -> forwardKey = false
                KeyEvent.VK_S -> backwardKey = false
                KeyEvent.VK_A -> leftKey = false
                KeyEvent.VK_D -> rightKey = false
                KeyEvent.VK_SHIFT -> boostKey = false
                KeyEvent.VK_SPACE -> fireKey = false
            }
        }
    }
}