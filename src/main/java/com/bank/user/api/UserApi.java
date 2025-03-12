package com.bank.user.api;

import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.UserGetAllResponse;
import com.bank.user.dto.UserRequest;
import com.bank.user.dto.UserResponse;
import com.bank.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(("api/v3/user"))
@CrossOrigin
@Tag(name = "User api", description = "APIs for Users")
public class UserApi {

    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Создать нового пользователя", description = "Добавляет нового пользователя в систему.")
    public ResponseEntity<SimpleResponse> createUser(@Valid @RequestBody UserRequest user) {
        SimpleResponse response = userService.createUser(user);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/getUserById/{userId}")
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его уникальному идентификатору (ID)."
    )
    public UserResponse getUserById(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/list")
    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей."
    )
    public List<UserGetAllResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}
