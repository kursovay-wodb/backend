package ru.rent.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.rent.demo.dto.UserDto;
import ru.rent.demo.service.AdminService;
import ru.rent.demo.service.UserService;


@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController (AdminService adminService,
                            UserService userService){
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/find-user/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        return adminService.findUserById(id);
    }

    @PostMapping("/check")
    public boolean isAdmin (@RequestHeader("Authorization") String authHeader) {
        String token = userService.tokenFromHeader(authHeader);
        return adminService.isAdmin(token);
    }
}
