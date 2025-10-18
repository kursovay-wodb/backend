package ru.rent.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.rent.demo.dto.LoginRequestDto;
import ru.rent.demo.dto.RegistrationRequestDto;
import ru.rent.demo.dto.TokenDto;
import ru.rent.demo.entity.User;
import ru.rent.demo.repository.UserRepository;
import ru.rent.demo.security.JwtTokenProvider;
import ru.rent.demo.utils.auth.AuthProvider;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthService (UserService userService, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenDto localRegistration (RegistrationRequestDto registrationRequestDto) {

        if (userService.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = userService.createUser (
            registrationRequestDto.getEmail(),
            registrationRequestDto.getPassword(),
            registrationRequestDto.getName(),
            AuthProvider.LOCAL
        );

        Authentication authentication = createAuthentication(user);

        String token = jwtTokenProvider.generateToken(authentication);

        return new TokenDto(token, "Bearer", "Регистрация успешна пройдена");
    }

    public TokenDto localLogin(LoginRequestDto loginRequestDto) {

        User user = userService.authenticate(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );

        Authentication authentication = createAuthentication(user);
        String token = jwtTokenProvider.generateToken(authentication);

        return new TokenDto(token, "Bearer", "Вход выполнен успешно");
    }

    private Authentication createAuthentication(User user) {

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                user, null, authorities);
    }

    public boolean validateToken (String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public boolean validateTokenFromAuthorization (String authHeader) {

        String token = extractTokenFromHeader(authHeader);

        if (token == null) {
            throw new RuntimeException("Authorization header is missing or invalid");
        } else {
            return validateToken(token);
        }
        
    }
    private String extractTokenFromHeader (String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
