package com.wave.backend.user.controller;

import com.wave.backend.user.dto.UserResponse;
import com.wave.backend.user.dto.UserSummaryResponse;
import com.wave.backend.user.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.wave.backend.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PatchMapping("/me")
    public UserResponse updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return userService.updateCurrentUser(request);
    }

    @GetMapping("/search")
    public List<UserSummaryResponse> searchUsers(
            @RequestParam String q
    ) {
        return userService.searchUsers(q);
    }
}
