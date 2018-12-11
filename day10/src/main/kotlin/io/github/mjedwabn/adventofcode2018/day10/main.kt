package io.github.mjedwabn.adventofcode2018.day10

import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.KeyListener
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel

fun main(args: Array<String>) {
    EventQueue.invokeLater(::createAndShowGUI)
}

private fun createAndShowGUI() {
    val frame = Renderer()
    frame.isVisible = true
}

class Renderer : JFrame(), KeyListener {
    private val pointRegex: Regex = """position=<\s*(-?\d+),\s*(-?\d+)> velocity=<\s*(-?\d+),\s*(-?\d+)>""".toRegex()
    private var panel = MyPanel()
    private val points: List<Point>
    private var i = 0

    init {
        points = parseInput()
        panel.points = points
    }

    private fun parseInput(): List<Point> {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines().map { parsePoint(it) }
    }

    private fun parsePoint(point: String): Point {
        val matchResult = pointRegex.find(point)
        val (x, y, vx, vy) = matchResult!!.destructured
        return Point(x.toInt(), y.toInt(), vx.toInt(), vy.toInt())
    }

    override fun keyTyped(e: KeyEvent?) = Unit

    override fun keyPressed(e: KeyEvent?) {
        i += makeMove(e)
        panel.repaint()
        println("frame $i")
    }

    private fun makeMove(e: KeyEvent?): Int = when {
        e!!.keyCode == VK_UP -> move(100)
        e.keyCode == VK_DOWN -> move(50)
        e.keyCode == VK_LEFT -> moveBack()
        else -> move()
    }

    private fun move(times: Int = 1): Int {
        (0..times).forEach { points.forEach { p -> p.move() } }
        return times
    }

    private fun moveBack(): Int {
        points.forEach { it.moveBack() }
        return -1
    }

    override fun keyReleased(e: KeyEvent?) = Unit

    init {
        createUI()
    }

    private fun createUI() {
        addKeyListener(this)
        val scrollPane = ScrollPane()
        scrollPane.add(panel)
        add(scrollPane)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
    }
}

class MyPanel : JPanel() {
    var points: List<Point> = emptyList()

    init {
        preferredSize = Dimension(400, 400)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        val sortedByX = points.sortedBy { it.x }
        val sortedByY = points.sortedBy { it.y }

        val left = sortedByX.first().x
        val right = sortedByX.last().x
        val top = sortedByY.first().y
        val bottom = sortedByY.last().y
        val diffX = right - left
        val diffY = bottom - top
        size = Dimension(diffX, diffY)
        preferredSize = size
        minimumSize = size
        points.forEach { drawPoint(g2d, it, left, top) }
    }

    private fun drawPoint(g2d: Graphics2D, point: Point, left: Int, top: Int) {
        val x = point.x - left
        val y = point.y - top
        g2d.drawRect(x, y, 1, 1)
    }
}

data class Point(var x: Int, var y: Int, val vx: Int, val vy: Int) {
    fun move() {
        x += vx
        y += vy
    }

    fun moveBack() {
        x -= vx
        y -= vy
    }
}