package com.bbenefield.TodoJava.controllers;

import com.bbenefield.TodoJava.entities.Todo;
import com.bbenefield.TodoJava.services.TodoService;
import jakarta.persistence.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo createdTodo = todoService.create(todo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTodo.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTodo);
    }

    @PutMapping("{id}")
    public ResponseEntity<Todo> updateTodo(@RequestBody Todo todo) {
        Todo updatedTodo = todoService.update(todo);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({DataFormatException.class, PersistenceException.class})
    public ResponseEntity<String> handleDatabaseExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Database error, please try again later");
    }
}
