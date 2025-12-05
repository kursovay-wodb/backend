package ru.rent.demo.service;

import org.springframework.stereotype.Service;
import ru.rent.demo.dto.UserDto;
import ru.rent.demo.entity.User;
import ru.rent.demo.repository.UserRepository;
import ru.rent.demo.security.JwtTokenProvider;

import java.util.Set;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminService (UserRepository userRepository,
                         JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id = " + id + " не найден"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setRole(user.getRoles().toString());
        userDto.setProvider(user.getProvider().toString());
        userDto.setDealsAsOwner(user.getDealsAsOwner());
        userDto.setDealsAsCustomer(user.getDealsAsCustomer());

        return userDto;
    }

    public boolean isAdmin (String token) {
        Set<User.Role> role = jwtTokenProvider.getRolesFromToken(token);
        System.out.println(role);
        System.out.println(role.contains(User.Role.ROLE_ADMIN));
        return role.contains(User.Role.ROLE_ADMIN);
    }
}
