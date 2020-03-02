package connectors

import java.util

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser, JsonPointer, JsonToken, ObjectCodec}
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.{JsonNode, SerializerProvider}
import com.fasterxml.jackson.databind.node.JsonNodeType
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
import play.api.libs.json.{JsArray, Json}

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

  private val url: String = "/city/London/users"

  "ApiConnector" when {

    "getUsersRegisteredInLondon is called" should {

      "return a list of users if 200 response with valid JSON is returned" in {

        server.stubFor(
          get(urlEqualTo(url)).willReturn(
            aResponse()
              .withStatus(200)
              .withBody(Json.toJson(List(Fixtures.fakeUser)).toString())
          )
        )

        val result = connectorUnderTest.getUsersRegisteredInLondon

        result.futureValue mustBe Right(List(Fixtures.fakeUser))
      }

      "return a ConnectorError with correct message if no users are found" in {

        val errorCode = 404

        server.stubFor(
          get(urlEqualTo(url)).willReturn(
            aResponse()
              .withStatus(errorCode)
              .withBody("[]")
          )
        )

        val result = connectorUnderTest.getUsersRegisteredInLondon

        result.futureValue mustBe Left(ConnectorError(errorCode, "Could not find any users with City: London"))
      }

      "return a ConnectorError if call returns 5xx response" in {

        val errorCode = 500
        val errorBody = "An error occurred"

        server.stubFor(
          get(urlEqualTo(url)).willReturn(
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
          get(urlEqualTo(url)).willReturn(
            aResponse()
              .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
          )
        )

        val result = connectorUnderTest.getUsersRegisteredInLondon

        result.futureValue mustBe Left(ConnectorError(500, "Remotely closed"))
      }
    }
  }
}
