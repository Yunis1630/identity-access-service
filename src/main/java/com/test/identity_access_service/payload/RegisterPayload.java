package com.test.identity_access_service.payload;

import com.test.identity_access_service.model.Permission;
import com.test.identity_access_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPayload {
    private String username;
    private String name;
    private String surname;
    private String password;
    private List<Role> role;
    private List<Permission> permission;
}

