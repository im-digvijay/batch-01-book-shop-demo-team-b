package com.tw.bootcamp.bookshop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.Set;

import static com.tw.bootcamp.bookshop.user.UserTestBuilder.buildCreateUserRequest;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    void shouldCreateUserWhenCredentialsAreValid() throws Exception {
        String email = "testemail@test.com";
        CreateUserRequest userCredentials = buildCreateUserRequest();
        User user = new UserTestBuilder().withId(1L).withEmail(email).build();
        when(userService.create(userCredentials)).thenReturn(user);
        UserResponse userResponse = UserResponse.builder().id(user.getId().toString()).email(email).build();

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userCredentials))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(userResponse)));

        verify(userService, times(1)).create(userCredentials);
    }
}