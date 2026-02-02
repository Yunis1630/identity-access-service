package com.test.identity_access_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private String fullName;
    private String accessToken;
}

