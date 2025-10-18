package ru.rent.demo.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler,
                          JwtTokenFilter jwtTokenFilter) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
        this.oauth2AuthenticationFailureHandler = oauth2AuthenticationFailureHandler;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/index.html", "/static/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization")
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/*")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .failureHandler(oauth2AuthenticationFailureHandler)
                );

        // Добавляем JWT фильтр только для API endpoints, исключая OAuth2
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}