package com.test.identity_access_service.dto;

import com.test.identity_access_service.model.Permission;
import com.test.identity_access_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String name;
    private String surname;
    private List<Role> roles;
    private List<Permission> permissions;
}
