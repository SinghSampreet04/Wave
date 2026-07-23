package com.wave.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSummaryResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
}
