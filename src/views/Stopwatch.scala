package views

class Stopwatch {
  var start = System.currentTimeMillis()
  def elapsed = System.currentTimeMillis() - start
  def printElapsed(msg: String) = {
    val delta = elapsed
    new Thread(new Runnable {
      def run = {
        System.out.println(msg + " time ellapsed: " + delta + "ms")
      }
    }).start()
  }
  def restart = {
    start = System.currentTimeMillis()
      new Thread(new Runnable {
      def run = {
        System.out.println("------restart------")
      }
    }).start()
  }
}