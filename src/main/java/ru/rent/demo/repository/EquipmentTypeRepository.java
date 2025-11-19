package ru.rent.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rent.demo.datasource.routing.ReadOnlyRepository;
import ru.rent.demo.entity.EquipmentType;

import java.util.List;

@Repository
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Long> {

    @ReadOnlyRepository
    List<EquipmentType> findByCategory(String category);

    @ReadOnlyRepository
    @Query("SELECT DISTINCT et.category FROM EquipmentType et")
    List<String> findAllCategories();
}