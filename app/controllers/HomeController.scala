package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import models.Todo

import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.mvc._
import _root_.requests.TodoForm
import daos.TodoDao

@Singleton
class HomeController @Inject()(todoDao: TodoDao, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val todoForm: Form[TodoForm] = Form(
    mapping(
      "content" -> nonEmptyText
    )(TodoForm.apply)(TodoForm.unapply)
  )

  def index: Action[AnyContent] = Action.async { implicit req =>
    todoDao.all.map(todos => Ok(views.html.index(todoForm, todos)))
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    todoForm.bindFromRequest().fold(
      formWithErrors => {
        todoDao.all.map(todos => Ok(views.html.index(formWithErrors, todos)))
      },
      todo => {
        val content = todo.content
        todoDao.insert(Todo(0, content))
        todoDao.all.map(todos => Ok(views.html.index(todoForm.fill(todo), todos)))
      }
    )
  }

}
