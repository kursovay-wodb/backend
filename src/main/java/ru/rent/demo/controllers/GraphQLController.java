package ru.rent.demo.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.rent.demo.datasource.routing.ReadOnlyRepository;
import ru.rent.demo.dto.ProductCacheDto;
import ru.rent.demo.dto.ProductInputDto;
import ru.rent.demo.dto.rental.RentalContractDto;
import ru.rent.demo.entity.EquipmentType;
import ru.rent.demo.entity.Product;
import ru.rent.demo.entity.RentalContract;
import ru.rent.demo.repository.EquipmentTypeRepository;
import ru.rent.demo.repository.UserRepository;
import ru.rent.demo.security.JwtTokenProvider;
import ru.rent.demo.service.*;

import java.util.List;

@Controller
public class GraphQLController {

    private EquipmentTypeRepository equipmentTypeRepository;
    private ProductService productService;
    private AuthService authService;
    private JwtTokenProvider jwtTokenProvider;
    private RentalContractService rentalContractService;
    private UserService userService;
    private UserRepository userRepository;

    private CacheService cacheService;

    public GraphQLController (EquipmentTypeRepository equipmentTypeRepository,
                              ProductService productService,
                              AuthService authService,
                              JwtTokenProvider jwtTokenProvider,
                              RentalContractService rentalContractService,
                              UserService userService,
                              UserRepository userRepository,
                              CacheService cacheService) {
        this.equipmentTypeRepository =equipmentTypeRepository;
        this.productService = productService;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.rentalContractService = rentalContractService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }


    // Просмотр всех товаров
    @ReadOnlyRepository
    @QueryMapping
    public List<Product> products() {
        List<ProductCacheDto> cachedProducts = productService.findAll();
        return cacheService.convertToGraphQLProducts(cachedProducts);
    }

    // Все товары определённого человека
    @ReadOnlyRepository
    @QueryMapping
    public List<Product> productsByOwner(@Argument("ownerId") Long ownerId) {
        return productService.findByOwner(ownerId);
    }

    // Доступные товары
    @ReadOnlyRepository
    @QueryMapping
    public List<Product> availableProducts() {
        List<ProductCacheDto> cachedProducts = productService.findAvailableProducts();
        return cacheService.convertToGraphQLProducts(cachedProducts);
    }

    @ReadOnlyRepository
    @QueryMapping
    public Product productById(@Argument("productId") Long productId) {return productService.findById(productId);}


    // Все типы
    @ReadOnlyRepository
    @QueryMapping
    public List<EquipmentType> equipmentTypes() {
        return equipmentTypeRepository.findAll();
    }

    // По категории
    @ReadOnlyRepository
    @QueryMapping
    public List<EquipmentType> equipmentTypesByCategory(@Argument("category") String category) {
        return equipmentTypeRepository.findByCategory(category);
    }


    @ReadOnlyRepository
    @QueryMapping
    public List<RentalContract> rentalContractsByCustomer(@ContextValue String authorization) {
        Long customerId = userService.idFromHeaderAuthorization(authorization);
        return rentalContractService.findByCustomerId(customerId);
    }

    @ReadOnlyRepository
    @QueryMapping
    public List<RentalContract> rentalContractsByOwner(@ContextValue String authorization) {
        Long ownerId = userService.idFromHeaderAuthorization(authorization);
        return rentalContractService.findByOwnerId(ownerId);
    }

    @ReadOnlyRepository
    @QueryMapping
    public List<RentalContractDto> rentalContracts() {
        return rentalContractService.getAllRentalContracts();
    }


    // Создание заявки о выставление на аренду
    @MutationMapping
    public Product createProduct(@Argument("input") ProductInputDto input,
                                 @ContextValue String authorization) {

        Long ownerId = userService.idFromHeaderAuthorization(authorization);

        return productService.createProduct(input, ownerId);
    }

}