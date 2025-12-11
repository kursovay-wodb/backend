package ru.rent.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.rent.demo.datasource.routing.ReadOnlyRepository;
import ru.rent.demo.entity.RentalContract;

import java.util.List;

@Repository
public interface RentalContractRepository extends MongoRepository<RentalContract, String> {
    @ReadOnlyRepository
    List<RentalContract> findByCustomerId(Long customerId);
    @ReadOnlyRepository
    List<RentalContract> findByOwnerId(Long ownerId);
}