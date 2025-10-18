package ru.rent.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rent.demo.repository.UserRepository;
import ru.rent.demo.security.CustomOAuth2UserService;
import ru.rent.demo.security.JwtTokenProvider;
import ru.rent.demo.security.OAuth2AuthenticationFailureHandler;
import ru.rent.demo.security.OAuth2AuthenticationSuccessHandler;
import ru.rent.demo.service.UserService;

@Configuration
public class OAuth2Config {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    OAuth2Config (JwtTokenProvider jwtTokenProvider,
                  UserRepository userRepository,
                  UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository, userService);
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(jwtTokenProvider);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler();
    }
}
