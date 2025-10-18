package ru.rent.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rent.demo.entity.User;
import ru.rent.demo.repository.UserRepository;
import ru.rent.demo.utils.auth.AuthProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User createUser(String email, String password, String name, Set<User.Role> roles, AuthProvider authProvider) {

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password)); // Записываем хэш пароля
        user.setProvider(authProvider);
        user.setRoles(roles != null ? roles : Set.of(User.Role.ROLE_USER)); // по умолчанию USER

        User savedUser = userRepository.save(user);

        if (savedUser != null) {
            return savedUser;
        } else {
            throw new RuntimeException("Ошибка создания пользователя");
        }

    }

    // Для создания обычного пользователя
    public User createUser (String email, String password, String name, AuthProvider authProvider) {
        return createUser(email, password, name, Set.of(User.Role.ROLE_USER), authProvider);
    }

    // Для создания админа
    public User createAdmin (String email, String password, String name, AuthProvider authProvider) {
        return createUser(email, password, name, Set.of(User.Role.ROLE_ADMIN), authProvider);
    }

    public User authenticate(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        System.out.println("Input password: " + password);
        System.out.println("Stored hash: " + user.getPassword());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return user;
    }

    public Optional<User> findUserByEmail (String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUserOauth2(String email, String name, AuthProvider authProvider) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(null); // Записываем без пароля, т.к. через Google
        user.setProvider(authProvider);
        user.setRoles(Collections.singleton(User.Role.ROLE_USER));

        User savedUser = userRepository.save(user);

        if (savedUser != null) {
            return savedUser;
        } else {
            throw new RuntimeException("Ошибка создания пользователя");
        }
    }

}
