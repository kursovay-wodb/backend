package ru.rent.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rent.demo.datasource.routing.ReadOnlyRepository;
import ru.rent.demo.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @ReadOnlyRepository
    List<Product> findByOwnerId(Long ownerId);
    @ReadOnlyRepository
    List<Product> findByQuantityGreaterThan(int i);

    @ReadOnlyRepository
    @Query("SELECT p FROM Product p JOIN FETCH p.equipmentType WHERE p.owner.id = :ownerId")
    List<Product> findByOwnerIdWithEquipmentType(@Param("ownerId") Long ownerId);

    @ReadOnlyRepository
    @Query("SELECT p FROM Product p JOIN FETCH p.equipmentType WHERE p.quantity > 0")
    List<Product> findAvailableProductsWithEquipmentType();

    @ReadOnlyRepository
    @Query("SELECT p FROM Product p JOIN FETCH p.equipmentType")
    List<Product> findAllWithEquipmentType();

    @ReadOnlyRepository
    @Query("SELECT p FROM Product p JOIN FETCH p.equipmentType WHERE p.id = :id")
    Optional<Product> findByIdWithEquipmentType(@Param("id") Long id);

}
