package utils

import models.User

object Fixtures {

  val fakeUserInLondon: User =
    User(
      1,
      "John",
      "Doe",
      "john.doe@gmail.com",
      "192.168.0.1",
      101.6751,
      -2.7620,
      Some("London")
    )

  val fakeUserNearLondon: User =
    User(
      1,
      "John",
      "Doe",
      "john.doe@gmail.com",
      "192.168.0.1",
      51.5074,
      0.1278,
      None
    )

  val listOfUsers: List[User] =
    List(
      fakeUserInLondon,
      fakeUserNearLondon
    )
}
