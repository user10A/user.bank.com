package com.bank.user.ApiTest;

import com.bank.user.api.UserApi;
import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.UserGetAllResponse;
import com.bank.user.dto.UserRequest;
import com.bank.user.dto.UserResponse;
import com.bank.user.exceptions.NotFoundException;
import com.bank.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("Успешное создание пользователя")
    void testCreateUser_Success() throws Exception {
        UserRequest userRequest = new UserRequest("Джон Уик", "джон@example.com");
        SimpleResponse simpleResponse = new SimpleResponse(HttpStatus.CREATED, "Пользователь успешно сохранён!");

        when(userService.createUser(any(UserRequest.class))).thenReturn(simpleResponse);

        mockMvc.perform(post("/api/v3/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated()) // Проверяем что вернется 201
                .andExpect(jsonPath("$.message").value("Пользователь успешно сохранён!"));

        verify(userService, times(1)).createUser(any(UserRequest.class));
    }

    @Test
    @DisplayName("Не успешное создание пользователя")
    void testCreateUser_EmailExists() throws Exception {

        UserRequest userRequest = new UserRequest("Джон Уик", "джон@example.com");
        SimpleResponse simpleResponse = new SimpleResponse(HttpStatus.CONFLICT, "Пользователь с такой почтой уже существует!");

        when(userService.createUser(any(UserRequest.class))).thenReturn(simpleResponse);

        mockMvc.perform(post("/api/v3/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict()) // Теперь вернёт 409
                .andExpect(jsonPath("$.message").value("Пользователь с такой почтой уже существует!"));

        verify(userService, times(1)).createUser(any(UserRequest.class));
    }


    @Test
    @DisplayName("Успешное получение пользователя")
    void testGetUserById_Success() throws Exception {
        long userId = 1L;
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUserName("Джон Уик");
        userResponse.setEmail("джон@example.com");

        when(userService.getUser(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v3/user/getUserById/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.userName").value("Джон Уик"))
                .andExpect(jsonPath("$.email").value("джон@example.com"));

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    @DisplayName("Должен вернуть 404, если пользователь не найден")
    void testGetUserById_NotFound() throws Exception {
        long userId = 99L;

        when(userService.getUser(userId)).thenThrow(new NotFoundException("Пользователь с ID " + userId + " не найден."));

        mockMvc.perform(get("/api/v3/user/getUserById/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь с ID " + userId + " не найден."));

        verify(userService, times(1)).getUser(userId);
    }


    @Test
    @DisplayName("Успешное получение всех пользователей")
    void testGetAllUsers_Success() throws Exception {
        UserGetAllResponse user1 = new UserGetAllResponse(1L, "Джон Уик", "джон@example.com", LocalDateTime.now());
        UserGetAllResponse user2 = new UserGetAllResponse(2L, "Джон Уик 4", "джон4@example.com", LocalDateTime.now());

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/v3/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userName").value("Джон Уик"))
                .andExpect(jsonPath("$[0].email").value("джон@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userName").value("Джон Уик 4"))
                .andExpect(jsonPath("$[1].email").value("джон4@example.com"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Должен вернуть 500 , Ошибка при получении списка пользователей")
    void testGetAllUsersThrowsException() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Ошибка при получении списка пользователей"));

        mockMvc.perform(get("/api/v3/user/list"))
                .andExpect(status().isInternalServerError()) // Проверяем что статус 500
                .andExpect(jsonPath("$.message").value("Ошибка при получении списка пользователей"))
                .andExpect(jsonPath("$.httpStatus").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.exceptionClassName").value("RuntimeException"));

        verify(userService, times(1)).getAllUsers();
    }
}