package ru.rent.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rent.demo.entity.BookingContract;
import ru.rent.demo.entity.Product;
import ru.rent.demo.entity.User;
import ru.rent.demo.repository.BookingRepository;
import ru.rent.demo.repository.UserRepository;
import ru.rent.demo.security.JwtTokenProvider;
import ru.rent.demo.utils.auth.AuthProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final BookingContractService bookingContractService;

    private final CacheService cacheService;

    private final  ProductService productService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, BookingContractService bookingContractService, CacheService cacheService, ProductService productService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bookingContractService = bookingContractService;
        this.cacheService = cacheService;
        this.productService = productService;
    }

    private User createUser(String email, String password, String name, Set<User.Role> roles, AuthProvider authProvider) {

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password)); // Записываем хэш пароля
        user.setProvider(authProvider);
        user.setRoles(roles != null ? roles : Set.of(User.Role.ROLE_USER)); // по умолчанию USER
        user.setDealsAsCustomer(0);
        user.setDealsAsOwner(0);
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
        user.setDealsAsCustomer(0);
        user.setDealsAsOwner(0);

        User savedUser = userRepository.save(user);

        if (savedUser != null) {
            return savedUser;
        } else {
            throw new RuntimeException("Ошибка создания пользователя");
        }
    }

    public String tokenFromHeader (String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new RuntimeException("Valid Authorization header required");
        }

        return authorization.substring(7);
    }

    public Long idFromHeaderAuthorization (String authorization) {

        String token = this.tokenFromHeader(authorization);

        return jwtTokenProvider.getUserIdFromToken(token);
    }

    public void updateUserRentStatsPlusOneDeal (Long ownerId, Long customerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Арендодатель не найден"));
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден"));

        try {
            owner.setDealsAsOwner(owner.getDealsAsOwner() + 1);
            customer.setDealsAsCustomer(customer.getDealsAsCustomer() + 1);
            userRepository.save(owner);
            userRepository.save(customer);
            cacheService.evictProductCache(); // место аннотации, т.к. вызывается в том же классе
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении статисктики пользователей: " + e.getMessage());
        }
    }

    public void updateUserRentStatsPlusOneDeal (String bookingId, Long customerId) {
        BookingContract bookingContract = bookingContractService.findContractById(bookingId);

        Product product = productService.findById(bookingContract.getProductId());

        User owner = userRepository.findById(product.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("Арендодатель не найден"));
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден"));

        try {
            owner.setDealsAsOwner(owner.getDealsAsOwner() + 1);
            customer.setDealsAsCustomer(customer.getDealsAsCustomer() + 1);
            userRepository.save(owner);
            userRepository.save(customer);
            cacheService.evictProductCache(); // место аннотации, т.к. вызывается в том же классе
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении статисктики пользователей: " + e.getMessage());
        }
    }

    public void updateUserRentStatsMinusOneDeal (Long ownerId, Long customerId) {
        User owner = checkUserAndReturn(ownerId, "Арендодатель не найден");
        User customer = checkUserAndReturn(customerId, "Покупатель не найден");

        try {
            owner.setDealsAsOwner(owner.getDealsAsOwner() - 1);
            customer.setDealsAsCustomer(customer.getDealsAsCustomer() - 1);
            userRepository.save(owner);
            userRepository.save(customer);
            cacheService.evictProductCache(); // место аннотации, т.к. вызывается в том же классе
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при откате статисктики пользователей: " + e.getMessage());
        }
    }

    public User checkUserAndReturn (Long userId, String errorMsg) {
        User user;
        return user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(errorMsg));
    }

}
