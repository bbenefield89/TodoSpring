package com.bbenefield.TodoJava.data.repositories;

import com.bbenefield.TodoJava.entities.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
