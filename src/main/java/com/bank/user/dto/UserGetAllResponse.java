package com.bank.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetAllResponse {
    private Long id;
    private String userName;
    private String email;
    private LocalDateTime creationDate;
}
