package ru.rent.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rent.demo.datasource.routing.ReadOnlyRepository;
import ru.rent.demo.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @ReadOnlyRepository
    Optional<User> findByEmail(String email);
    @ReadOnlyRepository
    boolean existsByEmail(String email);
}
