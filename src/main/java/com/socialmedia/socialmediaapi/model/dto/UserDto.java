package com.socialmedia.socialmediaapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmedia.socialmediaapi.model.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID uuid;
    private String firstName;
    private String lastName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    private Role role;

}
