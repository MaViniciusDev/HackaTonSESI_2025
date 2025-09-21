package org.utopia.hackatonsesi_2025.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.utopia.hackatonsesi_2025.users.dto.UserRegisterDTO;
import org.utopia.hackatonsesi_2025.users.dto.UserResponseDTO;
import org.utopia.hackatonsesi_2025.users.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@Valid @RequestBody UserRegisterDTO dto) {
        return service.register(dto);
    }
}

