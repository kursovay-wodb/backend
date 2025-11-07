package ru.rent.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rent.demo.dto.booking.BookingContractDto;
import ru.rent.demo.dto.ErrorResponse;
import ru.rent.demo.dto.booking.BookingContractInputDto;
import ru.rent.demo.service.BookingContractService;
import ru.rent.demo.service.ProductService;
import ru.rent.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/v1/booking-contracts")
public class BookingContractController {


    private UserService userService;
    private BookingContractService bookingContractService;
    private ProductService productService;


    public BookingContractController(UserService userService, BookingContractService bookingContractService, ProductService productService) {
        this.userService = userService;
        this.bookingContractService = bookingContractService;
        this.productService = productService;
    }

    // Создание брони
    @PostMapping("/")
    public ResponseEntity<?> createContract(@RequestBody BookingContractInputDto bookingContractInputDto,
                                            @RequestHeader("Authorization") String authorization) {
        try {
            Long customerId = userService.idFromHeaderAuthorization(authorization);
            BookingContractDto contractDto = bookingContractService.createBookingContract(bookingContractInputDto, customerId);
            return ResponseEntity.ok(contractDto);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Ошибка на сервере"));
        }
    }

    // Все брони пользователя
    @GetMapping ("/")
    public ResponseEntity<?> getAllUserBookingContracts (@RequestHeader("Authorization") String authorization) {
        try {
            Long customerId = userService.idFromHeaderAuthorization(authorization);
            List<BookingContractDto> bookingContractDtos = bookingContractService.getAllContractsByUserId(customerId);
            return ResponseEntity.ok(bookingContractDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Ошибка на сервере"));
        }
    }

    // Данные конкретной брони
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingContractById (@PathVariable String bookingId) {
        try {
            BookingContractDto bookingContractDto = bookingContractService.getBookingContractById(bookingId);
            return ResponseEntity.ok(bookingContractDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении договора брони");
        }
    }

    // Удаление брони
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBookingContractById(@PathVariable String bookingId) {
        try {
            productService.updateProductQuantityFromBookingContract(bookingId);

            try {
                bookingContractService.deleteBookingContractById(bookingId);
                return ResponseEntity.ok("Договор бронирования отменён");

            } catch (Exception e) {
                productService.rollbackUpdateProductQuantityFromBookingContract(bookingId);
                return ResponseEntity.internalServerError().body("Ошибка при получении договора брони");
            }

           } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка обновлении кол-ва товара при отмене брони");
        }
    }
}
