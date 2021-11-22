package com.voshodnerd.BeatySalon.repository;

import com.voshodnerd.BeatySalon.jpa.*;
import com.voshodnerd.BeatySalon.model.Booking;
import com.voshodnerd.BeatySalon.model.Cashier;
import com.voshodnerd.BeatySalon.model.OperationType;
import com.voshodnerd.BeatySalon.model.TransactionElement;
import com.voshodnerd.BeatySalon.model.authentication.RoleName;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.service.CashierService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CashierIntegrationServiceTest {
    @Value("${uuid.cashier}")
    private String cashierId;

    public final String ADMIN_USERNAME = "'admin";
    public final Float SUM_AFTER_BOOKING_DONE = 89.5f;


    @Test
    @DisplayName("Check repository is loaded")
    void checkRepositoryIsLoaded() {
        assertThat(bookingRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(cashierRepository).isNotNull();
        assertThat(transactionRepository).isNotNull();
        assertThat(materialRepository).isNotNull();
    }


    CashierService cashierService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    CashierRepository cashierRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MaterialRepository materialRepository;


    @BeforeEach
    public void setup() {
        System.out.println("Before each test");
        Users user = new Users();
        user.setActive(true);
        user.setName("name");
        user.setEmail("email@mail.ru");
        user.setUsername(ADMIN_USERNAME);
        user.setPassword("password");
        user.setRole(RoleName.ROLE_ADMIN);
        user = userRepository.save(user);
        for (int i = 0; i < 3; i++) {
            Booking booking = new Booking();
            booking.setTotalSum(new BigDecimal(5l));
            booking.setMaster(user);
            booking.setUsers(user);
            bookingRepository.save(booking);
        }
        Cashier cashier = new Cashier();
        cashier.setId(UUID.fromString(cashierId));
        cashier.setTotalSum(new BigDecimal(100l));
        cashierRepository.save(cashier);

        CashierService cashierService = new CashierService(cashierRepository, transactionRepository, bookingRepository, materialRepository);
        cashierService.setCashierId(cashierId);
        this.cashierService = cashierService;
    }

   /* @Test
    void closeBooking() {
    }

    @Test
    void wasteMaterialOfBooking() {
    } */

    @Test
    @DisplayName("Check out closing booking  and calculate ")
    void setBookingDone() {
        Users users = userRepository.findByUsername(ADMIN_USERNAME).orElseThrow(() -> new EntityNotFoundException(""));
        List<Booking> list = bookingRepository.findByMaster(users).orElseThrow(() -> new EntityNotFoundException(""));
        cashierService.setBookingDone(list);
        Cashier cashier = cashierRepository.findAll().stream().findFirst().orElseThrow(() -> new EntityNotFoundException(""));
        assertEquals(SUM_AFTER_BOOKING_DONE, cashier.getTotalSum().floatValue());
    }
}