package com.bank.user.repo;

import com.bank.user.dto.UserGetAllResponse;
import com.bank.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    @Query("select new com.bank.user.dto.UserGetAllResponse(u.id, u.userName, u.email, u.creationDate) from User u")
    List<UserGetAllResponse> getUsers();
    boolean existsByEmail (String email);

}
