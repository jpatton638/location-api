package controllers

import javax.inject._
import play.api.mvc._
import connectors.ApiConnector

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class LocationApiController @Inject()(apiConnector: ApiConnector) extends Controller {

  def getUsers: Action[AnyContent] = Action.async {
    implicit request =>

      apiConnector.getUsersRegisteredInLondon.map {
        case Right(users) =>
          Ok(s"$users")
        case Left(error) =>
          Status(error.code).apply(error.responseBody)
      }
  }
}
