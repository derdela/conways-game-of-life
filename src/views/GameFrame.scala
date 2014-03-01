package views

import swing._

object GameFrame extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "delas game of life"
    contents = new GameGrid(500, 400, 2)
    size = new Dimension(1000, 800)
  }
}