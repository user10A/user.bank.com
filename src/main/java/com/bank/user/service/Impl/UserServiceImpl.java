package com.bank.user.service.Impl;

import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.UserGetAllResponse;
import com.bank.user.dto.UserRequest;
import com.bank.user.dto.UserResponse;
import com.bank.user.entities.User;
import com.bank.user.exceptions.AlreadyExistsException;
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
        try {
            validateRequest(request);
            if (userRepository.existsByEmail(request.getEmail())) {
                log.warn("Попытка создания пользователя с дублирующим email: {}", request.getEmail());
                throw new AlreadyExistsException("Пользователь с такой почтой уже существует!");
            }

            User user = User.builder()
                    .userName(request.getUserName())
                    .email(request.getEmail())
                    .creationDate(LocalDateTime.now())
                    .build();

            userRepository.save(user);
            log.info("Пользователь успешно создан: {}", user);
            return  new SimpleResponse(HttpStatus.CREATED, "Пользователь успешно сохранён!");
        } catch (IllegalArgumentException | AlreadyExistsException e) {
            log.error("Ошибка при создании пользователя: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при создании пользователя: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
    }

    @Override
    public UserResponse getUser(Long userId) {
        try {
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
        } catch (NotFoundException e) {
            log.error("Ошибка при получении пользователя с ID {}: {}", userId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при получении пользователя с ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении пользователя", e);
        }
    }

    @Override
    public List<UserGetAllResponse> getAllUsers() {
        try {
            List<UserGetAllResponse> users = userRepository.getUsers();

            if (users.isEmpty()) {
                log.warn("Список пользователей пуст.");
            } else {
                log.info("Найдено пользователей: {}", users.size());
            }

            return users;
        } catch (Exception e) {
            log.error("Ошибка при получении списка пользователей: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении списка пользователей", e);
        }
    }

    private void validateRequest(UserRequest request) {
        if (request == null) {
            log.warn("Входящий запрос на создание пользователя пуст.");
            throw new IllegalArgumentException("Запрос не может быть пустым.");
        }
    }
}