package scala;
class Person(val name:String,val age:Int) {

}
//伴生对象
object Person{
  def getIdentityNo()= {"Hello Hanyu"}
}


object Test {
  def main(args: Array[String]) {
    println(Person.getIdentityNo());
    println(Person.toString);
  }
}
