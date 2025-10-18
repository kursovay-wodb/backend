package ru.rent.demo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errorMessage = exception.getMessage();
        String redirectUrl = frontendUrl + "/oauth2/redirect?error=" +
                URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}