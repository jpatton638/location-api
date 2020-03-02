package connectors

import config.ApiConfiguration
import javax.inject.Inject
import models.{ConnectorError, User}
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class ApiConnector @Inject()(config: ApiConfiguration, http: WSClient)(implicit ec: ExecutionContext) {

  private val logger = Logger(this.getClass)

  def getUsersRegisteredInLondon: Future[Either[ConnectorError, List[User]]] = {

    val urlToCall = s"${config.apiBaseUrl}/city/London/users"

    http.url(urlToCall).get().map { response =>

      response.status match {
        case 200 =>
          println(response.json)
          Right(response.json.asOpt[List[User]].getOrElse(List.empty))
        case 404 =>
          logger.warn(s"Could not find any users with City: London")
          Right(List.empty)
        case status @ _ =>
          logger.warn(s"Call failed with status $status")
          Left(ConnectorError(status, response.body))
      }
    } recover {
      case e: Exception =>
        logger.error(s"${e.getClass.getSimpleName} was throw by the connector")
        Left(ConnectorError(500, e.getLocalizedMessage))
    }
  }
}
