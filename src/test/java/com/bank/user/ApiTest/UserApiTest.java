package com.bank.user.ApiTest;

import com.bank.user.api.UserApi;
import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.UserGetAllResponse;
import com.bank.user.dto.UserRequest;
import com.bank.user.dto.UserResponse;
import com.bank.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApi.class)
class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("John Doe");
        userRequest.setEmail("testErkin@example.com");

        SimpleResponse simpleResponse = new SimpleResponse(HttpStatus.OK, "Пользователь успешно создан");

        when(userService.createUser(userRequest)).thenReturn(simpleResponse);

        // Выполняем запрос к API
        mockMvc.perform(post("/api/v3/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messageCode").value("Пользователь успешно создан")) // Проверяем правильное поле
                .andExpect(jsonPath("$.httpStatus").value("OK")); // Проверяем статус

        // Проверяем, что сервис вызван
        verify(userService, times(1)).createUser(userRequest);
    }


    @Test
    void testGetUserById() throws Exception {
        long userId = 1L;
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUserName("John Doe");
        userResponse.setEmail("john.doe@example.com");

        when(userService.getUser(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v3/user/getUserById/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserGetAllResponse user1 = new UserGetAllResponse(1L, "John Doe", "john.doe@example.com", LocalDateTime.now());
        UserGetAllResponse user2 = new UserGetAllResponse(2L, "Jane Doe", "jane.doe@example.com", LocalDateTime.now());

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/v3/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userName").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userName").value("Jane Doe"))
                .andExpect(jsonPath("$[1].email").value("jane.doe@example.com"));

        verify(userService, times(1)).getAllUsers();
    }
}
