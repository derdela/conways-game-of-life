package views

import scala.swing.Panel
import java.awt.Graphics2D
import java.awt.Color
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import com.sun.jmx.snmp.Timestamp

class GameGrid(width: Int, height: Int, pixelSize: Int) extends Panel {

  val pixels = new HashMap[(Int, Int), (Boolean, Boolean)]
  val living = new HashSet[(Int, Int)]
  val candidates = new HashSet[(Int, Int)]
  val grbuffer = new HashMap[(Int, Int), (Boolean, Boolean)]

  for {
    a <- 0 to width
    b <- 0 to height
  } yield {
    val alive = Math.random() < 0.1
    pixels += (a, b) -> (alive, false)
    if (alive) living += ((a, b))
  }
  background = Color.BLACK
  startTicker

  override def paint(gr: Graphics2D) = {
    living.foreach {
      pixel =>
        draw((pixel, pixels.get(pixel).get), gr)
    }
    grbuffer.clear
  }
  def update() = {
    living.foreach(pixel => calculate((pixel, pixels.get(pixel).get)))
    candidates.foreach(pixel => calculate((pixel, pixels.get(pixel).get)))
    candidates.clear
    pixels.foreach {
      pixel =>
        if (pixel._2._1 != pixel._2._2) {
          grbuffer += (pixel)
        }
        pixels.update(pixel._1, (pixel._2._2, pixel._2._2))
        if (!pixel._2._2) {
          living -= pixel._1
        } else {
          living += pixel._1
        }
    }
    repaint
  }
  def draw(pixel: ((Int, Int), (Boolean, Boolean)), gr: Graphics2D): Unit = {
    if (pixel._2._1) gr.setColor(Color.WHITE)
    else gr.setColor(Color.BLACK)

    gr.fillRect(pixel._1._1 * pixelSize, pixel._1._2 * pixelSize, pixelSize, pixelSize)
    //gr.fillPolygon(PixelUnit.hexagon(pixel._1._1, pixel._1._2, pixelSize))

  }

  def calculate(pixel: ((Int, Int), (Boolean, Boolean))): Unit = {
    var count = 0
    for {
      x <- -1 to 1
      y <- -1 to 1
      if (!(x == 0 && y == 0)
        && pixel._1._1 + x > 0
        && pixel._1._2 + y > 0
        && pixel._1._1 + x < width
        && pixel._1._2 + y < height)
    } yield {
      val neighbor = pixels.get((pixel._1._1 + x, pixel._1._2 + y))
      if (neighbor.get._1) count += 1
      else if (pixel._2._1) candidates += ((pixel._1._1 + x, pixel._1._2 + y))
    }

    if (count == 3 || (count == 2 && pixel._2._2)) {
      pixels.update(pixel._1, (pixel._2._1, true))
    } else {
      pixels.update(pixel._1, (pixel._2._1, false))
    }
  }

  def startTicker = {
    new Thread(new Runnable {
      def run() {
        while (true) {
          update
        }
      }
    }).start()
  }
}