package helpers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}

trait WireMockHelper extends BeforeAndAfterAll with BeforeAndAfterEach {
  this: Suite =>

  private val port: Int = 9999

  protected val server: WireMockServer = new WireMockServer(wireMockConfig().port(port))

  override def beforeAll(): Unit = {
    server.start()
    super.beforeAll()
  }

  override def beforeEach(): Unit = {
    server.start()
    super.beforeEach()
  }

  override def afterEach(): Unit = {
    server.stop()
    super.afterEach()
  }
}
