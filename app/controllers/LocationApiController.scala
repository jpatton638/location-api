package controllers

import javax.inject._
import play.api.mvc._
import connectors.ApiConnector
import models.{ConnectorError, Coordinates, User}
import play.api.Logger
import play.api.libs.json.Json
import utils.RadiusCalculator

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class LocationApiController @Inject()(apiConnector: ApiConnector, radiusCalculator: RadiusCalculator) extends Controller {

  private val logger: Logger = Logger(this.getClass)

  def getLondonUsers: Action[AnyContent] = Action.async {
    implicit request =>

      for {
        usersInLondon <- apiConnector.getUsersRegisteredInLondon
        allUsers <- apiConnector.getAllUsers
      } yield {
        (usersInLondon, allUsers) match {

          case (Right(londonUserList), Right(allUsersList)) =>

            val radiusFromLondon: Double = 50.0

            val filteredUsers: List[User] = allUsersList.filter { user =>
              radiusCalculator.isWithinRadius(
                Coordinates(user.latitude, user.longitude),
                radiusFromLondon
              )
            }

            val combinedList: List[User] = londonUserList ++ filteredUsers

            Ok(Json.toJson(combinedList).toString())

          case (_, Left(error)) =>
            processPartialError(error)

          case (Left(error), _) =>
            processPartialError(error)
        }
      }
  }

  private def processPartialError(error: ConnectorError): Result = {
    val errorMessage = s"Failed to retrieve users: Connector failed with status code ${error.code} and the following message: ${error.responseBody}"
    logger.error(errorMessage)
    ServiceUnavailable(errorMessage)
  }
}
