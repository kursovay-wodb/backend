package ru.rent.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.rent.demo.entity.User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oauth2User;
    private User user;

    public CustomOAuth2User(OAuth2User oauth2User, User user) {
        this.oauth2User = oauth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getName() {
        return user.getEmail();
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return user.getEmail();
    }
}