package views

import java.awt.Polygon

object PixelUnit {
  def hexagon(x: Int, y: Int, size: Int): Polygon = {
    val degree = Math.PI * 1 / 3
    val offset = (size * 0.5 * Math.cos(degree), size * 0.5 * Math.sin(degree))

    var center = (x * size - x * offset._2 * 0.5, y * size - y * size * 0.25 * Math.cos(degree))
    if (x % 2 == 0) {
      center = (center._1, center._2 - size * 0.5)
    }
    val polygon = new Polygon
    polygon.addPoint((center._1 + offset._1).asInstanceOf[Int], (center._2 + offset._2).asInstanceOf[Int])
    polygon.addPoint((center._1 + size / 2).asInstanceOf[Int], (center._2).asInstanceOf[Int])
    polygon.addPoint((center._1 + offset._1).asInstanceOf[Int], (center._2 - offset._2).asInstanceOf[Int])
    polygon.addPoint((center._1 - offset._1).asInstanceOf[Int], (center._2 - offset._2).asInstanceOf[Int])
    polygon.addPoint((center._1 - size / 2).asInstanceOf[Int], (center._2).asInstanceOf[Int])
    polygon.addPoint((center._1 - offset._1).asInstanceOf[Int], (center._2 + offset._2).asInstanceOf[Int])
    polygon
  }

  def pixel(x: Int, y: Int, size: Int): Polygon = {
    val polygon = new Polygon
    val x1 = (x * size, y * size)
    polygon.addPoint(x1._1, x1._2)
    polygon.addPoint(x1._1 + size, x1._2)
    polygon.addPoint(x1._1 + size, x1._2 + size)
    polygon.addPoint(x1._1, x1._2 + size)
    polygon
  }
}