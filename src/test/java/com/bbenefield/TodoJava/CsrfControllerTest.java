package com.bbenefield.TodoJava;

import com.bbenefield.TodoJava.controllers.CsrfController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CsrfController.class)
public class CsrfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void getCsrf_shouldReturnCsrf() throws Exception {
        mockMvc
                .perform(get("/api/csrf"))
                .andExpect(jsonPath("$.parameterName").value("_csrf"));
    }

}
