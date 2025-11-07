package ru.rent.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rent.demo.entity.BookingContract;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository <BookingContract, String> {
    List<BookingContract> findByCustomerId(Long customerId);
    Optional<BookingContract> findById(String bookingContractId);

}
