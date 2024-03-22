package com.bbenefield.TodoJava.services;

import com.bbenefield.TodoJava.data.repositories.TodoRepository;
import com.bbenefield.TodoJava.entities.Todo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    public Todo update(Todo todo) {
        Todo updatedTodo = new Todo();
        updatedTodo.setId(todo.getId());
        updatedTodo.setTitle(todo.getTitle());
        updatedTodo.setComplete(todo.isComplete());
        return todoRepository.save(updatedTodo);
    }
}
