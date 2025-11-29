package ru.rent.demo.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.rent.demo.dto.EquipmentTypeCacheDto;
import ru.rent.demo.dto.ProductCacheDto;
import ru.rent.demo.dto.ProductInputDto;
import ru.rent.demo.entity.*;
import ru.rent.demo.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final EquipmentTypeRepository equipmentTypeRepository;
    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final RentalContractRepository rentalContractRepository;


    public ProductService(EquipmentTypeRepository equipmentTypeRepository, ProductRepository productRepository, UserRepository userRepository, BookingRepository bookingRepository, RentalContractRepository rentalContractRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.rentalContractRepository = rentalContractRepository;
    }

    @Cacheable(value = "products", key = "'all'")
    public List<ProductCacheDto> findAll() {
        System.out.println("Запрос в реляционную БД");
        List<Product> products = productRepository.findAllWithEquipmentType();
        return products.stream()
                .map(this::convertToCacheDTO)
                .collect(Collectors.toList());
    }

    public List<Product> findByOwner(Long ownerId) {
        return productRepository.findByOwnerId(ownerId);
    }

    @Cacheable(value = "products", key = "'all_available'")
    public List<ProductCacheDto> findAvailableProducts() {
        System.out.println("Запрос в реляционную БД");
        List<Product> products = productRepository.findByQuantityGreaterThan(0);
        return products.stream()
                .map(this::convertToCacheDTO)
                .collect(Collectors.toList());

    }


    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product createProduct(ProductInputDto input, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + ownerId));

        EquipmentType equipmentType = equipmentTypeRepository.findById(input.getEquipmentTypeId())
                .orElseThrow(() -> new RuntimeException("Equipment type not found with id: " + input.getEquipmentTypeId()));

        Product product = new Product();
        product.setName(input.getName());
        product.setDescription(input.getDescription());
        product.setRentalPrice(input.getRentalPrice());
        product.setQuantity(input.getQuantity());
        product.setMinRentalPeriod(input.getMinRentalPeriod());
        product.setMaxRentalPeriod(input.getMaxRentalPeriod());
        product.setOwner(owner);
        product.setImage(input.getImage());
        product.setEquipmentType(equipmentType);

        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product updateProduct(Long id, ProductInputDto input) {
        Product product = findById(id);
        EquipmentType equipmentType = equipmentTypeRepository.findById(input.getEquipmentTypeId())
                .orElseThrow(() -> new RuntimeException("Equipment type not found with id: " + input.getEquipmentTypeId()));

        product.setName(input.getName());
        product.setDescription(input.getDescription());
        product.setRentalPrice(input.getRentalPrice());
        product.setQuantity(input.getQuantity());
        product.setMinRentalPeriod(input.getMinRentalPeriod());
        product.setMaxRentalPeriod(input.getMaxRentalPeriod());
        product.setEquipmentType(equipmentType);

        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductCacheDto convertToCacheDTO(Product product) {
        ProductCacheDto dto = new ProductCacheDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImage());
        dto.setQuantity(product.getQuantity());
        dto.setRentalPrice(product.getRentalPrice());
        dto.setMinRentalPeriod(product.getMinRentalPeriod());
        dto.setMaxRentalPeriod(product.getMaxRentalPeriod());

        if (product.getEquipmentType() != null) {
            EquipmentTypeCacheDto typeDTO = new EquipmentTypeCacheDto();
            typeDTO.setId(product.getEquipmentType().getId());
            typeDTO.setName(product.getEquipmentType().getName());
            typeDTO.setDescription(product.getEquipmentType().getDescription());
            typeDTO.setCategory(product.getEquipmentType().getCategory());
            dto.setEquipmentType(typeDTO);
        }

        return dto;
    }
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product updateProductQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("У арендодателя не достаточно товара для аренды. Сейчас: " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - quantity);
        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void rollbackProductQuantity(Product product, int quantity) {
        try {
            product.setQuantity(product.getQuantity() + quantity);
            productRepository.save(product);
            System.out.println("Кол-во товаров вернулось в исходное состояние: " + product.getId());
        } catch (Exception rollbackException) {
            System.err.println("CRITICAL: Ошибка при откате кол-ва товаров: " + product.getId());
        }
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void updateProductQuantityFromBookingContract(String bookingId) {
        BookingContract bookingContract = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Договор брони не найден"));

        Product product = productRepository.findById(bookingContract.getProductId())
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        product.setQuantity(product.getQuantity() + bookingContract.getQuantity());

        productRepository.save(product);
        System.out.println("Кол-во товаров обновлено");
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void rollbackUpdateProductQuantityFromBookingContract(String bookingId) {
        BookingContract bookingContract = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Договор брони не найден"));

        Product product = productRepository.findById(bookingContract.getProductId())
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        product.setQuantity(product.getQuantity() - bookingContract.getQuantity());

        productRepository.save(product);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void updateProductQuantityFromRentalContract(String rentalContractId) {
        RentalContract rentalContract = rentalContractRepository.findById(rentalContractId)
                .orElseThrow(() -> new RuntimeException("Договор аренды не найден"));
        System.out.println("Бронь " + rentalContract.getIdBooking());
        updateProductQuantityFromBookingContract(rentalContract.getIdBooking());


    }
}