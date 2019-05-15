package component.learning

import org.scalatest.{BeforeAndAfterEach, FunSuite}

class LearningComponentTest extends FunSuite with BeforeAndAfterEach {

  val s1 = Seq[Int](2, 4, 5, 1)
  val s2 = Seq[Int](2, 4, 5, 1)
  val size = s1.size

  override def beforeEach() {

  }

  test("testLabelData") {
    val ans = LearningComponent.labelData(s1, s2, size)
    println(ans)
  }

}
