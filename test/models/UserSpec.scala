package models

import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.Json
import utils.Fixtures

class UserSpec extends WordSpec with MustMatchers {

  "User" must {

    "serialise and de-serialise correctly" in {

      Json.toJson(Fixtures.fakeUser).as[User] mustBe Fixtures.fakeUser
    }
  }
}
