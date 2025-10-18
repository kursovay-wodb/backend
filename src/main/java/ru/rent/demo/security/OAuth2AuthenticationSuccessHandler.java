package ru.rent.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import ru.rent.demo.entity.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    private final JwtTokenProvider tokenProvider;

    
    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Очищаем контекст безопасности
        SecurityContextHolder.clearContext();

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oauth2User.getUser();

        // Генерируем токен
        String token = tokenProvider.generateToken(authentication);

        // Перенаправляем на фронтенд
        String redirectUrl = String.format("%s/oauth2/redirect?token=%s&email=%s&name=%s",
                frontendUrl,
                token,
                URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8),
                URLEncoder.encode(user.getName(), StandardCharsets.UTF_8));

        response.sendRedirect(redirectUrl);
    }
}