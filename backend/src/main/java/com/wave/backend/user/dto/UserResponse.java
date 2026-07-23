package com.wave.backend.user.dto;

import com.wave.backend.user.entity.UserRole;
import com.wave.backend.user.entity.PresenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private UserRole role;

    private String avatar;

    private String bio;

    private PresenceStatus presencePreference;

}
