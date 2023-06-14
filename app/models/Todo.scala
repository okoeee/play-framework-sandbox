package models

case class Todo(
    id: Int,
    content: String,
    done: Boolean
)

object Todo {
  private var todos: List[Todo] = List(Todo(1, "掃除", false))

  def all: List[Todo] = todos

  def add(todo: Todo): Unit = {
    todos = todo +: todos
  }

  def update(todo: Todo): Unit = {}

  def delete(id: Int): Unit = {
    todos = todos.filterNot(_.id == id)
  }
}
