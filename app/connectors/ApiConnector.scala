package connectors

import config.ApiConfiguration
import javax.inject.Inject
import models.{ConnectorError, User}
import play.api.Logger
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

class ApiConnector @Inject()(config: ApiConfiguration, http: WSClient)(implicit ec: ExecutionContext) {

  private val logger: Logger = Logger(this.getClass)

  def getUsersRegisteredInLondon: Future[Either[ConnectorError, List[User]]] = {

    val url = s"${config.apiBaseUrl}/city/London/users"

    callApi(url).map { response =>
      processApiResponse(response, "Could not find any users with City: London")
    } recover {
      case e: Exception =>
        Left(recoverConnectorFailure(e))
    }
  }

  def getAllUsers: Future[Either[ConnectorError, List[User]]] = {

    val url = s"${config.apiBaseUrl}/users"

    callApi(url).map { response =>
      processApiResponse(response, "Could not find any users")
    } recover {
      case e: Exception =>
        Left(recoverConnectorFailure(e))
    }
  }

  private def callApi(urlToCall: String): Future[WSResponse] = http.url(urlToCall).get()

  private def processApiResponse(response: WSResponse, notFoundMessage: String): Either[ConnectorError, List[User]] = {
    response.status match {

      case 200 =>
        Right(response.json.asOpt[List[User]].getOrElse(List.empty))

      case status @ 404 =>
        val errorMessage = "Could not find any users"
        logger.warn(errorMessage)
        Left(ConnectorError(status, errorMessage))

      case status @ _ =>
        logger.error(s"Call failed with status $status")
        Left(ConnectorError(status, response.body))
    }
  }

  private def recoverConnectorFailure(exception: Exception): ConnectorError = {
    logger.error(s"${exception.getClass.getSimpleName} was throw by the connector")
    ConnectorError(500, exception.getLocalizedMessage)
  }
}
