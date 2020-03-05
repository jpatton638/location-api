package models

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import utils.Fixtures

class UserSpec extends PlaySpec {

  "User" must {

    "serialise and de-serialise correctly" in {

      Json.toJson(Fixtures.fakeUserNearLondon).as[User] mustBe Fixtures.fakeUserNearLondon
    }
  }
}
