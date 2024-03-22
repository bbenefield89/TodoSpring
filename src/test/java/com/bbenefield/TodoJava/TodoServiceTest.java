package com.bbenefield.TodoJava;

import com.bbenefield.TodoJava.data.repositories.TodoRepository;
import com.bbenefield.TodoJava.entities.Todo;
import com.bbenefield.TodoJava.services.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void findAll_shouldReturnTodos() {
        Todo todo1 = new Todo();
        todo1.setTitle("First Todo");
        todo1.setComplete(false);

        Todo todo2 = new Todo();
        todo2.setTitle("Second Todo");
        todo2.setComplete(true);

        List<Todo> allTodos = Arrays.asList(todo1, todo2);

        given(todoRepository.findAll())
                .willReturn(allTodos);

        assertThat(todoService.findAll())
                .isEqualTo(allTodos);

        verify(todoRepository, times(1))
                .findAll();
    }

    @Test
    public void create_shouldReturnCreatedTodo() {
        Todo todo = new Todo();
        todo.setTitle("New Todo");

        given(todoRepository.save(any(Todo.class)))
                .willReturn(todo);

        assertThat(todoService.create(todo))
                .isEqualTo(todo);

        verify(todoRepository, times(1))
                .save(any(Todo.class));
    }

    @Test
    public void delete_shouldReturnVoid() {
        doNothing()
                .when(todoRepository)
                .deleteById(anyLong());

        todoService.deleteById(anyLong());

        verify(todoRepository, times(1))
                .deleteById(anyLong());
    }

    @Test
    public void update_shouldReturnUpdatedTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Updated Todo");
        todo.setComplete(true);

        given(todoRepository.save(any(Todo.class)))
                .willReturn(todo);

        assertThat(todoService.update(todo))
                .isEqualTo(todo);

        verify(todoRepository, times(1))
                .save(any(Todo.class));
    }

}
