package controllers

import models.Todo

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request: Request[AnyContent] =>
    val message = "Hello Yokoe"
    val todoList = Todo.all
    Ok(views.html.index(todoList))
  }

  def create = Action { implicit request =>
    Ok(views.html.todo.create())
  }
  
}
