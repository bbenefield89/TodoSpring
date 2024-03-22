package com.bbenefield.TodoJava;

import com.bbenefield.TodoJava.controllers.TodoController;
import com.bbenefield.TodoJava.entities.Todo;
import com.bbenefield.TodoJava.services.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @Test
    @WithMockUser
    public void handleDatabaseExceptions_shouldReturnString() throws Exception {
        doThrow(new PersistenceException())
                .when(todoService)
                .findAll();

        mockMvc
                .perform(get("/api/todos"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Database error, please try again later"));
    }

    @Test
    @WithMockUser
    public void getAllTodos_shouldReturnTodos() throws Exception {
        Todo todo1 = new Todo();
        todo1.setTitle("First Todo");
        todo1.setComplete(false);

        Todo todo2 = new Todo();
        todo2.setTitle("Second Todo");
        todo2.setComplete(true);

        List<Todo> allTodos = Arrays.asList(todo1, todo2);

        given(todoService.findAll())
                .willReturn(allTodos);

        mockMvc
                .perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("First Todo"))
                .andExpect(jsonPath("$[1].title").value("Second Todo"));
    }

    @Test
    @WithMockUser
    public void createTodo_shouldReturnCreatedTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("New Todo");

        given(todoService.create(any(Todo.class)))
                .willReturn(todo);

        mockMvc
                .perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo))
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Todo"));
    }

    @Test
    @WithMockUser
    public void deleteTodo_shouldReturnVoid() throws Exception {
        doNothing()
                .when(todoService)
                .deleteById(anyLong());

        mockMvc
                .perform(delete("/api/todos/1")
                        .with(csrf())
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    public void updateTodo_shouldReturnUpdatedTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Updated Todo");
        todo.setComplete(true);

        given(todoService.update(any(Todo.class)))
                .willReturn(todo);

        mockMvc
                .perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.complete").value(true));
    }

}
