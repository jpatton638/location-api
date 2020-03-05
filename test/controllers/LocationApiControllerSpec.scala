package controllers

import connectors.ApiConnector
import models.ConnectorError
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import org.mockito.Mockito._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import utils.{Fixtures, RadiusCalculator}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class LocationApiControllerSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar {

  private lazy val fakeCalculator: RadiusCalculator = app.injector.instanceOf[RadiusCalculator]

  private val mockApiConnector: ApiConnector = mock[ApiConnector]

  private val controllerUnderTest: LocationApiController =
    new LocationApiController(mockApiConnector, fakeCalculator)

  "HomeController.getLondonUsers" must {

    "return OK and a user if connector calls are successful" in {

      when(
        mockApiConnector.getUsersRegisteredInLondon
      ) thenReturn Future(Right(List(Fixtures.fakeUserInLondon)))

      when(
        mockApiConnector.getAllUsers
      ) thenReturn Future(Right(List(Fixtures.fakeUserNearLondon)))

      val result = controllerUnderTest.getLondonUsers.apply(FakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe Json.toJson(Fixtures.listOfUsers).toString()
    }

    "return a Service Unavailable with the correct body" when {

      val error: ConnectorError = ConnectorError(502, "Could not reach Location API")

      val errorMessage: String =
        s"Failed to retrieve users: Connector failed with status code ${error.code} and the following message: ${error.responseBody}"

      "getUsersRegisteredInLondon call returns a ConnectorError" in {

        when(
          mockApiConnector.getUsersRegisteredInLondon
        ) thenReturn Future(Left(error))

        val result = controllerUnderTest.getLondonUsers.apply(FakeRequest())

        status(result) mustBe SERVICE_UNAVAILABLE
        contentAsString(result) mustBe errorMessage
      }

      "getAllUsers call returns a ConnectorError" in {

        when(
          mockApiConnector.getAllUsers
        ) thenReturn Future(Left(error))

        val result = controllerUnderTest.getLondonUsers.apply(FakeRequest())

        status(result) mustBe SERVICE_UNAVAILABLE
        contentAsString(result) mustBe errorMessage
      }
    }
  }
}
