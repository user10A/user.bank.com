package com.bank.user.service;

import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.UserGetAllResponse;
import com.bank.user.dto.UserRequest;
import com.bank.user.dto.UserResponse;
import java.util.List;

public interface UserService {
      SimpleResponse createUser (UserRequest request);
      UserResponse getUser (Long id);
      List<UserGetAllResponse> getAllUsers ();
}
