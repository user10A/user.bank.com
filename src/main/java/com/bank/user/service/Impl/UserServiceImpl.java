package com.bank.user.service.Impl;

import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.UserGetAllResponse;
import com.bank.user.dto.UserRequest;
import com.bank.user.dto.UserResponse;
import com.bank.user.entities.User;
import com.bank.user.exceptions.NotFoundException;
import com.bank.user.repo.UserRepo;
import com.bank.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;

    @Override
    public SimpleResponse createUser(UserRequest request) {
        validateRequest(request);
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Попытка создания пользователя с дублирующим email: {}", request.getEmail());
            return createSimpleResponse(HttpStatus.CONFLICT, "Пользователь с такой почтой уже существует!");
        }

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .creationDate(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("Пользователь успешно создан: {}", user);
        return createSimpleResponse(HttpStatus.CREATED, "Пользователь успешно сохранён!");
    }

    @Override
    public UserResponse getUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .email(user.getEmail())
                        .creationDate(user.getCreationDate())
                        .build())
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID {} не найден", userId);
                    return new NotFoundException("Пользователь с ID " + userId + " не найден.");
                });
    }

    @Override
    public List<UserGetAllResponse> getAllUsers() {
        List<UserGetAllResponse> users = userRepository.getUsers();

        if (users.isEmpty()) {
            log.warn("Список пользователей пуст.");
        } else {
            log.info("Найдено пользователей: {}", users.size());
        }

        return users;
    }
    private SimpleResponse createSimpleResponse(HttpStatus status, String message) {
        return new SimpleResponse(status, message);
    }

    private void validateRequest(UserRequest request) {
        if (request == null) {
            log.warn("Входящий запрос на создание пользователя пуст.");
            throw new IllegalArgumentException("Запрос не может быть пустым.");
        }
    }
}