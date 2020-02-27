package controllers

import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

class HomeControllerSpec extends PlaySpec with OneAppPerTest {

  "HomeController GET" should {

    "return OK status with correct content" in {
      val controller = new HomeController
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentAsString(home) mustBe "Hello World!"
    }
  }
}
