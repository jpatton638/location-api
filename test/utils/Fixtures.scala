package utils

import models.User

object Fixtures {

  val fakeUser: User =
    User(
      1,
      "John",
      "Doe",
      "john.doe@gmail.com",
      "192.168.0.1",
      123.123456,
      -987.654321,
      Some("Newcastle")
    )
}
