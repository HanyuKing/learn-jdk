package scala

class Companion {
  def hello() { println("Hello (class)") } // [1]
}
object Companion {
  def hallo() { println("Hallo (object)") } // [2]
  def hello() { println("Hello (object)") } // [3]
}