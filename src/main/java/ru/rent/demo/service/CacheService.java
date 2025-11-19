package ru.rent.demo.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.rent.demo.dto.ProductCacheDto;
import ru.rent.demo.entity.EquipmentType;
import ru.rent.demo.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CacheService {



    public List<Product> convertToGraphQLProducts(List<ProductCacheDto> cachedDTOs) {
        return cachedDTOs.stream()
                .map(this::convertToGraphQLProduct)
                .collect(Collectors.toList());
    }

    public Product convertToGraphQLProduct(ProductCacheDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        product.setQuantity(dto.getQuantity());
        product.setRentalPrice(dto.getRentalPrice());
        product.setMinRentalPeriod(dto.getMinRentalPeriod());
        product.setMaxRentalPeriod(dto.getMaxRentalPeriod());

        if (dto.getEquipmentType() != null) {
            EquipmentType equipmentType = new EquipmentType();
            equipmentType.setId(dto.getEquipmentType().getId());
            equipmentType.setName(dto.getEquipmentType().getName());
            equipmentType.setDescription(dto.getEquipmentType().getDescription());

            // Обрабатываем возможный null для category
            String category = dto.getEquipmentType().getCategory();
            equipmentType.setCategory(category != null ? category : "default");

            product.setEquipmentType(equipmentType);
        }

        return product;
    }


    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void evictProductCache() {
        // Пустой метод - нужна только аннотация
        // Spring создает прокси, который обрабатывает кэширование
        // Spring не создает прокси для внутренних вызовов - поэтому нужно тут, для обхода этого
    }
}
