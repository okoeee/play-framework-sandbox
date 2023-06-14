package controllers

import models.Todo

import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.mvc._
import _root_.requests.TodoForm

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val todoForm: Form[TodoForm] = Form(
    mapping(
      "content" -> nonEmptyText
    )(TodoForm.apply)(TodoForm.unapply)
  )

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val message = "Hello Yokoe"
    val todoList = Todo.all
    Ok(views.html.index(todoList))
  }

  def create = Action { implicit request =>
    Ok(views.html.todo.create(todoForm))
  }

  def postCreate = Action { implicit request =>
    todoForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(views.html.todo.create(formWithErrors))
      },
      todo => {
        //todo todo保存の処理
        println(todo)
        Redirect(routes.HomeController.index())
      }
    )
  }

}
