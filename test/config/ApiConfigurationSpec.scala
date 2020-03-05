package config

import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration

class ApiConfigurationSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar {

  private val mockConfig: Configuration = mock[Configuration]

  private val fakeUrl: String = "http://www.fakesite.com/fakePath"

  private val fakeDouble: Double = 120.5

  private def exceptionMessage(key: String): String =
    s"Could not retrieve '$key' from application.conf"

  lazy val classUnderTest: ApiConfiguration = new ApiConfiguration(mockConfig)

  "ApiConfiguration" when {

    "apiBaseUrl is called" must {

      "return the correct value from application.conf" in {

        when(mockConfig.getString(any(), any())
        ) thenReturn Some(fakeUrl)

        classUnderTest.apiBaseUrl mustBe fakeUrl
      }

      "throw a RuntimeException if the config value is empty" in {

        when(mockConfig.getString(any(), any())
        ) thenReturn None

        the [RuntimeException] thrownBy {
          classUnderTest.apiBaseUrl
        } must have message exceptionMessage("bpdts-api.base-url")
      }
    }

    "londonLatitude is called" must {

      "return the correct value from application.conf" in {

        when(mockConfig.getDouble(any())
        ) thenReturn Some(fakeDouble)

        classUnderTest.londonLatitude mustBe fakeDouble
      }

      "throw a RuntimeException if the config value is empty" in {

        when(mockConfig.getDouble(any())
        ) thenReturn None

        the [RuntimeException] thrownBy {
          classUnderTest.londonLatitude
        } must have message exceptionMessage("london-coordinates-latitude")
      }
    }

    "londonLongitude is called" must {

      "return the correct value from application.conf" in {

        when(mockConfig.getDouble(any())
        ) thenReturn Some(fakeDouble)

        classUnderTest.londonLongitude mustBe fakeDouble
      }

      "throw a RuntimeException if the config value is empty" in {

        when(mockConfig.getDouble(any())
        ) thenReturn None

        the [RuntimeException] thrownBy {
          classUnderTest.londonLongitude
        } must have message exceptionMessage("london-coordinates-longitude")
      }
    }
  }
}
