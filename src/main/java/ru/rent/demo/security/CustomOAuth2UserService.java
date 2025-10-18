package ru.rent.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.rent.demo.entity.User;
import ru.rent.demo.repository.UserRepository;
import ru.rent.demo.service.UserService;
import ru.rent.demo.utils.auth.AuthProvider;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private final UserRepository userRepository;
    private final UserService userService;

    public CustomOAuth2UserService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // В род. методе уже реализована: отправка на API GOOGLE, получение данных пользователя
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return new CustomOAuth2User(oAuth2User,existingUser.get());
        } else {
            name = (name != null ? name : email.split("@")[0]); // если имя не пришло
            User user = userService.createUserOauth2(email, name, AuthProvider.GOOGLE);
            return new CustomOAuth2User(oAuth2User, user);
        }
        
    }

}