package ru.rent.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rent.demo.dto.LoginRequestDto;
import ru.rent.demo.dto.RegistrationRequestDto;
import ru.rent.demo.dto.TokenDto;
import ru.rent.demo.security.JwtTokenProvider;
import ru.rent.demo.service.AuthService;
import ru.rent.demo.service.UserService;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    AuthController (AuthService authService,
                    UserService userService,
                    JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Аутентификация через oAuth2 google
    @GetMapping("/google/success")
    public ResponseEntity<TokenDto> oauth2Success(@RequestParam String token) {
        TokenDto tokenDto = new TokenDto(
                token,
                "Bearer",
                "Успешная аутентификация через OAuth2"
        );

        return ResponseEntity.ok(tokenDto);
    }

    // Локальная регистрация (email + пароль)
    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        try {
            TokenDto tokenDto = authService.localRegistration(registrationRequestDto);
            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of(
                            "error", "Registration failed",
                            "message", e.getMessage()
                    ));
        }
    }

    // Локальный вход (email + пароль)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            TokenDto tokenDto = authService.localLogin(loginRequestDto);
            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of(
                            "error", "Login failed",
                            "message", e.getMessage()
                    ));
        }
    }

    // Проверка валидности JWT токена
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {


        System.out.println(authHeader);
        boolean isValid = authService.validateTokenFromAuthorization(authHeader);


        if (isValid) {
        return ResponseEntity.ok()
                .body(java.util.Map.of(
                        "valid", true,
                        "message", "Token is valid"
                ));
        } else {
            return ResponseEntity.status(401)
                    .body(java.util.Map.of(
                            "valid", false,
                            "message", "Token is invalid"
                    ));
        }

    }

}
