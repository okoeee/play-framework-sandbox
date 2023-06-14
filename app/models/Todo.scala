package models

case class Todo(
    id: Long,
    content: String,
)

//object Todo {
//  private var todos: List[Todo] = List(Todo(1, "掃除"))
//
//  def all: List[Todo] = todos
//
//  def add(todo: Todo): Unit = {
//    todos = todo +: todos
//  }
//
//  def update(todo: Todo): Unit = {}
//
//  def delete(id: Int): Unit = {
//    todos = todos.filterNot(_.id == id)
//  }
//}
