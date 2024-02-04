package daos

import models.Todo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TodoDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Todos = TableQuery[TodosTable]

  def all: Future[Seq[Todo]] = db.run(Todos.result)

  def findById(todo: Todo): Future[Option[Todo]] = db.run(Todos.filter(_.id === todo.id).result.headOption)

  def insert(todo: Todo): Future[Unit] = db.run(Todos += todo).map { _ =>
    ()
  }

  def update(todo: Todo): Future[Unit] = {
    db.run(Todos.filter(_.id === todo.id).map(_.content).update(todo.content)).map(_ => ())
  }

  def delete(todo: Todo): Future[Unit] = {
    db.run(Todos.filter(_.id === todo.id).delete).map(_ => ())
  }

  def sample = {
    val todosQuery = Todos.filter(_.id === 1L)
    val action = for {
      todos <- todosQuery.result
      _ <- Todos += Todo(0, "test")
    } yield ()
    db.run(action.transactionally)

    val q1 = Todos += Todo(0, "test")
    val q2 = Todos += Todo(0, "test")
    DBIO.sequence(Seq(q1, q2))

  }

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "TODO") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def content = column[String]("CONTENT")
    def * = (id, content) <> (Todo.tupled, Todo.unapply _)
  }

}
