package controllers

import connectors.ApiConnector
import models.ConnectorError
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import org.mockito.Matchers._
import org.mockito.Mockito._
import utils.Fixtures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class LocationApiControllerSpec extends PlaySpec with OneAppPerTest with MockitoSugar {

  private val mockApiConnector = mock[ApiConnector]
  private val controllerUnderTest = new LocationApiController(mockApiConnector)

  "HomeController" when {

    "getUsersRegisteredInLondon is called" must {

      "return OK and a user if connector returns a list of users" in {

        when(
          mockApiConnector.getUsersRegisteredInLondon
        ) thenReturn Future(Right(List(Fixtures.fakeUser)))

        val result = controllerUnderTest.getUsers.apply(FakeRequest())

        status(result) mustBe OK
        contentAsString(result) mustBe List(Fixtures.fakeUser).toString()
      }

      "return 404 if the connector returned an empty list of users" in {

        when(
          mockApiConnector.getUsersRegisteredInLondon
        ) thenReturn Future(Right(List.empty))

        val result = controllerUnderTest.getUsers.apply(FakeRequest())

        status(result) mustBe NOT_FOUND
        contentAsString(result) mustBe s"Could not find users from London"
      }

      "return an ConnectorError with the correct code and body if connector returned an error" in {

        val error = ConnectorError(502, "Could not reach Location API")

        when(
          mockApiConnector.getUsersRegisteredInLondon
        ) thenReturn Future(Left(error))

        val result = controllerUnderTest.getUsers.apply(FakeRequest())

        status(result) mustBe error.code
        contentAsString(result) mustBe error.responseBody
      }
    }
  }
}
