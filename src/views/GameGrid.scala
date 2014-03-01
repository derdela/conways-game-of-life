package views

import scala.swing.Panel
import java.awt.Graphics2D
import java.awt.Color
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import java.util.Calendar

class GameGrid(width: Int, height: Int, pixelSize: Int) extends Panel {

  val pixels = new HashMap[(Int, Int), (Boolean, Boolean)]
  val living = new HashSet[(Int, Int)]
  val candidates = new HashSet[(Int, Int)]
  val grbuffer = new HashMap[(Int, Int), (Boolean, Boolean)]
  for {
    a <- 0 to width
    b <- 0 to height
  } yield {
    val alive = Math.random() < 0.05
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
  }

  def calculate(pixel: ((Int, Int), (Boolean, Boolean))): Unit = {
    var count = 0
    for {
      x <- -1 to 1
      y <- -1 to 1
      if (!(x == 0 && y == 0)
        && pixel._1._1 + x > 0
        && pixel._1._2 + y > 0)
    } yield {
      val neighborKey = ((pixel._1._1 + x) % width, (pixel._1._2 + y) % height);
      val neighborValue = pixels.get(neighborKey)
      if (neighborValue.get._1) {
        count += 1
      } else if (pixel._2._1) {
        candidates += (neighborKey)
      }
    }

    if (count == 3 || (count == 2 && pixel._2._2)) {
      pixels.update(pixel._1, (pixel._2._1, true))
    } else {
      pixels.update(pixel._1, (pixel._2._1, false))
    }
  }

  def startTicker = {
    var count = 0;

    new Thread(new Runnable {
      def run() {
        while (true) {
          update

        }
      }
    }).start()
  }
}