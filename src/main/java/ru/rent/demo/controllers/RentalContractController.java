package ru.rent.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rent.demo.dto.rental.RentalContractDto;
import ru.rent.demo.service.RentalContractService;
import ru.rent.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/v1/rental-contracts")
public class RentalContractController {

    private final RentalContractService rentalContractService;
    private final UserService userService;

    public RentalContractController(RentalContractService rentalContractService, UserService userService) {
        this.rentalContractService = rentalContractService;
        this.userService = userService;
    }

    // Получение все контрактов аренды
    @GetMapping("/")
    public ResponseEntity<?> getAllRentalContracts () {
        try {
            List<RentalContractDto> rentalContractDtos = rentalContractService.getAllRentalContracts();
            return ResponseEntity.ok(rentalContractDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении договора аренды");
        }
    }

    // Получение всех контрактов аренды определённого пользователя как арендатора
    @GetMapping("/user/customer")
    public ResponseEntity<?> getAllRentalContractsAsCustomerByUserId (@RequestHeader("Authorization") String authorization) {
        try {
            Long customerId = userService.idFromHeaderAuthorization(authorization);
            List<RentalContractDto> rentalContractDtos = rentalContractService.getAllRentalContractsAsCustomerByUserId(customerId);
            return ResponseEntity.ok(rentalContractDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении договора аренды");
        }
    }

    // Получение всех контрактов аренды определённого пользователя как арендатора
    @GetMapping("/user/owner")
    public ResponseEntity<?> getAllRentalContractsAsOwnerByUserId (@RequestHeader("Authorization") String authorization) {
        try {
            Long customerId = userService.idFromHeaderAuthorization(authorization);
            List<RentalContractDto> rentalContractDtos = rentalContractService.getAllRentalContractsAsOwnerByUserId(customerId);
            return ResponseEntity.ok(rentalContractDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении договора аренды");
        }
    }

    // Получение всех контрактов аренды определённого пользователя для админа
    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllRentalContractsByUserId (@PathVariable Long userId) {
        try {
            List<RentalContractDto> rentalContractDtos = rentalContractService.getAllRentalContractsByUserId(userId);
            return ResponseEntity.ok(rentalContractDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении договора аренды");
        }
    }

}
