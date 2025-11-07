package ru.rent.demo.service;

import org.springframework.stereotype.Service;
import ru.rent.demo.dto.booking.BookingContractDto;
import ru.rent.demo.dto.booking.BookingContractInputDto;
import ru.rent.demo.entity.BookingContract;
import ru.rent.demo.entity.Product;
import ru.rent.demo.repository.BookingRepository;
import ru.rent.demo.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingContractService {

    private final BookingRepository bookingRepository;
    private final ProductRepository productRepository;

    private final ProductService productService;

    public BookingContractService(BookingRepository bookingRepository, ProductRepository productRepository, ProductService productService) {
        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public BookingContractDto createBookingContract(BookingContractInputDto bookingContractInputDto, Long customerId) {
        Product product = null;
        Long ownerId = null;

        try {
            // Обновление в реляционной бд
            product = productService.updateProductQuantity(bookingContractInputDto.getProductId(), bookingContractInputDto.getQuantity());

                try {
                    // Создание договора
                    BookingContract contract = createBookingContractInMongo(bookingContractInputDto, product, customerId, ownerId);
                    BookingContractDto contractDto = new BookingContractDto(contract);
                    return contractDto;

                } catch (Exception mongoException) {
                    // Если была ошибка в монго - откат изменения в реляционной бд
                    productService.rollbackProductQuantity(product, bookingContractInputDto.getQuantity());
                    throw new RuntimeException("Ошибка создания договора MongoDB", mongoException);
                }

        } catch (Exception e) {
            // Если была ошибка обновления данных в реляционной БД - откат изменения в реляционной бд
            throw new RuntimeException("Ошибка при создании договора об аренде оборудования: " + e.getMessage(), e);
        }
    }

    private BookingContract createBookingContractInMongo(BookingContractInputDto input, Product product,
                                                 Long customerId, Long ownerId) {
        // Создаём договор брони
        BookingContract contract = new BookingContract();

        contract.setCustomerId(customerId);
        contract.setProductName(product.getName());
        contract.setProductId(product.getId());
        contract.setStartRentDate(input.getStartRentDate());
        contract.setEndRentDate(input.getEndRentDate());
        contract.setQuantity(input.getQuantity());
        contract.setRentalPrice(product.getRentalPrice());
        contract.setBookingDate(LocalDate.now());

        return bookingRepository.save(contract);
    }


    public List<BookingContractDto> getAllContractsByUserId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookingContractDto convertToDto(BookingContract bookingContract) {
        if (bookingContract == null) {
            return null;
        }

        BookingContractDto dto = new BookingContractDto();
        dto.setId(bookingContract.getId());
        dto.setStartRentDate(bookingContract.getStartRentDate());
        dto.setEndRentDate(bookingContract.getEndRentDate());
        dto.setQuantity(bookingContract.getQuantity());
        dto.setProductName(bookingContract.getProductName());
        dto.setProductId(bookingContract.getProductId());
        dto.setRentalPrice(bookingContract.getRentalPrice());
        dto.setBookingDate(bookingContract.getBookingDate());
        dto.setPaid(bookingContract.isPaid());

        return dto;
    }

    public BookingContract findContractById(String bookingContractId) {
        return bookingRepository.findById(bookingContractId)
                .orElseThrow(() -> new RuntimeException("Договор бронирования не найден"));
    }

    public BookingContractDto getBookingContractById(String id) {
        BookingContractDto bookingContractDto = new BookingContractDto(findContractById(id));
        return bookingContractDto;
    }

    public void deleteBookingContractById(String bookingId) {
        BookingContract bookingContract = findContractById(bookingId);
        bookingRepository.delete(bookingContract);
    }

    public void changeStatusOnPaid(String bookingContractId) {
        BookingContract bookingContract = findContractById(bookingContractId);
        bookingContract.setPaid(true);
        bookingRepository.save(bookingContract);
    }
}
