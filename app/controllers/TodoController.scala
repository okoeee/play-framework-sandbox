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

import scala.concurrent.Future

@Singleton
class TodoController @Inject()(todoDao: TodoDao, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val todoForm: Form[TodoForm] = Form(
    mapping(
      "content" -> nonEmptyText
    )(TodoForm.apply)(TodoForm.unapply)
  )

  def index: Action[AnyContent] = Action.async { implicit req =>
    todoDao.all.map(todos => Ok(views.html.index(todos)))
  }

  def create: Action[AnyContent] = Action { implicit req =>
    Ok(views.html.todo.create(todoForm))
  }

  def createAction: Action[AnyContent] = Action.async { implicit request =>
    todoForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(Ok(views.html.todo.create(formWithErrors)))
        },
        todo => {
          val content = todo.content
          todoDao.insert(Todo(0, content)) flatMap { _ =>
            Future.successful(Redirect(routes.TodoController.index))
          }
        }
      )
  }

  def edit(id: Long): Action[AnyContent] = Action.async { implicit req =>
    todoDao.findById(Todo(id, "")).map {
      case Some(todo) => Ok(views.html.todo.edit(todoForm.fill(TodoForm(todo.content)), todo))
      case None       => Redirect(routes.TodoController.index)
    }
  }

  def editAction(preId: Long): Action[AnyContent] = Action.async { implicit req =>
    todoForm
      .bindFromRequest()
      .fold(
        formWithError => {
          todoDao.findById(Todo(preId, "")).map {
            case Some(todo) => Ok(views.html.todo.edit(formWithError, todo))
            case None       => Redirect(routes.TodoController.index)
          }
        },
        todo => {
          todoDao.update(Todo(preId, todo.content))
          todoDao.all.map(todos => Redirect(routes.TodoController.index))
        }
      )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit req =>
    todoDao.delete(Todo(id, ""))
    todoDao.all.map(todos => Redirect(routes.TodoController.index))
  }

}
