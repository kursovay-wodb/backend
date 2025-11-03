package ru.rent.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rent.demo.entity.PaymentContract;
import ru.rent.demo.service.PaymentContractService;
import ru.rent.demo.service.RentalContractService;
import ru.rent.demo.service.UserService;

@RestController
@RequestMapping("api/v1/payment-contracts")
public class PaymentController {

    private final PaymentContractService paymentContractService;
    private final RentalContractService rentalContractService;
    private final UserService userService;

    public PaymentController(PaymentContractService paymentContractService, RentalContractService rentalContractService, UserService userService) {
        this.paymentContractService = paymentContractService;
        this.rentalContractService = rentalContractService;
        this.userService = userService;
    }

    @PostMapping("/{bookingContractId}")
    public ResponseEntity<?> createPaymentContract(@PathVariable String bookingContractId,
                                                   @RequestHeader("Authorization") String authorization) {
        try {
            Long customerId = userService.idFromHeaderAuthorization(authorization);
            PaymentContract paymentContract = paymentContractService.createPaymentContract(bookingContractId, customerId);

            //Создаём договор аренды, после оплаты
            rentalContractService.createRentContract(bookingContractId, paymentContract, customerId);

            return ResponseEntity.ok(paymentContract);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при создании договора оплаты");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentContract(@PathVariable String id) {

        try {
            PaymentContract paymentContract = paymentContractService.getPaymentContractById(id);
            return ResponseEntity.ok(paymentContract);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении договора оплаты");
        }
    }

}
