package connectors

import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import config.ApiConfiguration
import utils.{Fixtures, WireMockHelper}
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, urlEqualTo}
import com.github.tomakehurst.wiremock.http.Fault
import models.ConnectorError
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global

class ApiConnectorSpec extends PlaySpec
  with GuiceOneAppPerSuite with WireMockHelper with ScalaFutures with IntegrationPatience {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
    .configure(
      "bpdts-api.base-url" -> server.baseUrl()
    ).build()

  private lazy val fakeApiConfig = app.injector.instanceOf[ApiConfiguration]
  private lazy val fakeWSHttp = app.injector.instanceOf[WSClient]

  private lazy val connectorUnderTest = new ApiConnector(fakeApiConfig, fakeWSHttp)

  private val londonUsersUrl: String = "/city/London/users"
  private val allUsersUrl: String = "/users"

  "ApiConnector" when {

    "getUsersRegisteredInLondon is called" should {

      "return a list of users if 200 response with valid JSON is returned" in {

        server.stubFor(
          get(urlEqualTo(londonUsersUrl)).willReturn(
            aResponse()
              .withStatus(200)
              .withBody(Json.toJson(List(Fixtures.fakeUserNearLondon)).toString())
          )
        )

        val result = connectorUnderTest.getUsersRegisteredInLondon

        result.futureValue mustBe Right(List(Fixtures.fakeUserNearLondon))
      }

      "return a ConnectorError with correct message if no users are found" in {

        val errorCode = 404

        server.stubFor(
          get(urlEqualTo(londonUsersUrl)).willReturn(
            aResponse()
              .withStatus(errorCode)
              .withBody("[]")
          )
        )

        val result = connectorUnderTest.getUsersRegisteredInLondon

        result.futureValue mustBe Left(ConnectorError(errorCode, "Could not find any users"))
      }

      "return a ConnectorError if call returns 5xx response" in {

        val errorCode = 500
        val errorBody = "An error occurred"

        server.stubFor(
          get(urlEqualTo(londonUsersUrl)).willReturn(
            aResponse()
              .withStatus(errorCode)
              .withBody(errorBody)
          )
        )

        val result = connectorUnderTest.getUsersRegisteredInLondon

        result.futureValue mustBe Left(ConnectorError(errorCode, errorBody))
      }

      "return a ConnectorError with 500 code if an exception is thrown by the GET call" in {

        server.stubFor(
          get(urlEqualTo(londonUsersUrl)).willReturn(
            aResponse()
              .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
          )
        )

        val result = connectorUnderTest.getUsersRegisteredInLondon

        result.futureValue mustBe Left(ConnectorError(500, "Remotely closed"))
      }
    }

    "getAllUsers is called" should {

      "return a list of users if 200 response with valid JSON is returned" in {

        server.stubFor(
          get(urlEqualTo(allUsersUrl)).willReturn(
            aResponse()
              .withStatus(200)
              .withBody(Json.toJson(List(Fixtures.fakeUserNearLondon)).toString())
          )
        )

        val result = connectorUnderTest.getAllUsers

        result.futureValue mustBe Right(List(Fixtures.fakeUserNearLondon))
      }

      "return a ConnectorError with correct message if no users are found" in {

        val errorCode = 404

        server.stubFor(
          get(urlEqualTo(allUsersUrl)).willReturn(
            aResponse()
              .withStatus(errorCode)
              .withBody("[]")
          )
        )

        val result = connectorUnderTest.getAllUsers

        result.futureValue mustBe Left(ConnectorError(errorCode, "Could not find any users"))
      }

      "return a ConnectorError if call returns 5xx response" in {

        val errorCode = 500
        val errorBody = "An error occurred"

        server.stubFor(
          get(urlEqualTo(allUsersUrl)).willReturn(
            aResponse()
              .withStatus(errorCode)
              .withBody(errorBody)
          )
        )

        val result = connectorUnderTest.getAllUsers

        result.futureValue mustBe Left(ConnectorError(errorCode, errorBody))
      }

      "return a ConnectorError with 500 code if an exception is thrown by the GET call" in {

        server.stubFor(
          get(urlEqualTo(allUsersUrl)).willReturn(
            aResponse()
              .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
          )
        )

        val result = connectorUnderTest.getAllUsers

        result.futureValue mustBe Left(ConnectorError(500, "Remotely closed"))
      }
    }
  }
}
