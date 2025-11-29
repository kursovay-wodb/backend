package ru.rent.demo.service;

import org.springframework.stereotype.Service;
import ru.rent.demo.dto.rental.RentalContractDto;
import ru.rent.demo.entity.PaymentContract;
import ru.rent.demo.entity.RentalContract;
import ru.rent.demo.repository.ProductRepository;
import ru.rent.demo.repository.RentalContractRepository;
import ru.rent.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalContractService {

    private CacheService cacheService;
    private RentalContractRepository rentalContractRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public RentalContractService(CacheService cacheService, RentalContractRepository rentalContractRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cacheService = cacheService;
        this.rentalContractRepository = rentalContractRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<RentalContract> findByCustomerId(Long customerId) {
        return rentalContractRepository.findByCustomerId(customerId);
    }

    public List<RentalContract> findByOwnerId(Long ownerId) {
        return rentalContractRepository.findByOwnerId(ownerId);
    }

    public RentalContract findById(String id) {
        return rentalContractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найден договор аренды: " + id));
    }


    public RentalContractDto createRentContract(PaymentContract paymentContract) {

        RentalContract rentalContract = new RentalContract();
        rentalContract.setCustomerId(paymentContract.getCustomerId());
        rentalContract.setOwnerId(paymentContract.getOwnerId());
        rentalContract.setIdBooking(paymentContract.getBookingId());
        rentalContract.setIdPayment(paymentContract.getId());
        rentalContractRepository.save(rentalContract);

        return new RentalContractDto(rentalContract);

    }

    public List<RentalContractDto> getAllRentalContracts() {
        return rentalContractRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RentalContractDto> getAllRentalContractsAsCustomerByUserId(Long userId) {
        return rentalContractRepository.findByCustomerId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RentalContractDto> getAllRentalContractsAsOwnerByUserId(Long userId) {
        return rentalContractRepository.findByOwnerId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RentalContractDto> getAllRentalContractsByUserId(Long userId) {
        List<RentalContractDto> listAsOwner = getAllRentalContractsAsOwnerByUserId(userId);
        System.out.println(listAsOwner);
        System.out.println("-=========================");
        List<RentalContractDto> listAsCustomer = getAllRentalContractsAsCustomerByUserId(userId);
        System.out.println(listAsCustomer);
        System.out.println("-=========================");

        List<RentalContractDto> allContracts = new ArrayList<>();
        allContracts.addAll(listAsOwner);
        allContracts.addAll(listAsCustomer);
        System.out.println(allContracts);
        System.out.println("-=========================");

        return allContracts;
    }

    private RentalContractDto convertToDto(RentalContract rentalContract) {

        if (rentalContract == null) {
            return null;
        }

        RentalContractDto dto = new RentalContractDto(rentalContract);
        return dto;
    }


    public void addRefundInContract(String rentalId, String refundId) {
         RentalContract rentalContract = findById(rentalId);
         rentalContract.setIdRefund(refundId);
         rentalContractRepository.save(rentalContract);
    }
}