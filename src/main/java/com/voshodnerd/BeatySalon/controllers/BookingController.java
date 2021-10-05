package com.voshodnerd.BeatySalon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voshodnerd.BeatySalon.jpa.BookingRepository;
import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.Booking;
import com.voshodnerd.BeatySalon.model.StatusBooking;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.service.BookingService;
import com.voshodnerd.BeatySalon.service.CashierService;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
@Tag(name = "Бронирования контроллер", description = "Методы по работе с брониированием")
@Slf4j
public class BookingController {
    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final CashierService cashierService;
    private final ObjectMapper mapper;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Создание нового бронирования",
            description = "Создание нового бронирования"
    )
    public ResponseEntity<?> createNewBooking(@Valid @RequestBody BookingDTO bookingDTO) throws JsonProcessingException {
        log.info(mapper.writeValueAsString(bookingDTO));
        ApiResponse response = bookingService.bookingValidation(bookingDTO);
        if (response.getSuccess()) {
            Booking booking = bookingRepository.save((Booking) response.getBody());
            log.info("Saved booking  {}", mapper.writeValueAsString(booking));
            response.setBody(booking);
            BookingDTO bkg = bookingService.toBookingDTO((Booking) response.getBody());
            response.setBody(bkg);
            response.setMessage(MessageConstant.BOOKING_IS_SUCCESSFUL);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(true, "Booking created Successfully", bookingDTO), HttpStatus.OK);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Валидация нового бронирования",
            description = "Валидирование бронирования перед созданием"
    )
    public ResponseEntity<?> validateNewBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        ApiResponse response = bookingService.bookingValidation(bookingDTO);
        if (response.getSuccess()) {
            BookingDTO bkg = bookingService.toBookingDTO((Booking) response.getBody());
            response.setBody(bkg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            summary = "Обновление бронирования",
            description = "Обновление бронирования"
    )
    public ResponseEntity<?> updateBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        bookingDTO = bookingService.updateBooking(bookingDTO);
        return new ResponseEntity(new ApiResponse(true, "Booking updated Successfully", bookingDTO), HttpStatus.OK);
    }


    @PutMapping("/close")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Закрытие бронирования",
            description = "Закрытие записи"
    )
    public ResponseEntity<?> closeBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        bookingDTO = bookingService.updateBooking(bookingDTO);
        cashierService.closeBooking(bookingDTO);
        cashierService.wasteMaterialOfBooking(bookingDTO);
        return new ResponseEntity(new ApiResponse(true, "Booking closed Successfully", bookingDTO), HttpStatus.OK);
    }

    @GetMapping("/done")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Выплаты платы за бронирование ",
            description = "Выплаты платы за бронирование"
    )
    public ResponseEntity<?> doneBookingFormMaster(@RequestParam(name = "userId") Long userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            return new ResponseEntity(new ApiResponse(false, "Booking done unSuccessfully"), HttpStatus.OK);
        Optional<List<Booking>> optionalBooking = bookingRepository.findByMaster(optionalUser.get());
        if (!optionalBooking.isPresent())
            return new ResponseEntity(new ApiResponse(false, "Booking done unSuccessfully"), HttpStatus.OK);
        cashierService.setBookingDone(optionalBooking.get());
        return new ResponseEntity(new ApiResponse(true, "Booking closed Successfully"), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> listBooking() {
        List<BookingDTO> lst = bookingService.getAllBooking();
        return new ResponseEntity<List<BookingDTO>>(lst, HttpStatus.OK);
    }

    @GetMapping("/byUserId")
    public ResponseEntity<?> getBookingByStatusAndUserId(@RequestParam(name = "status") StatusBooking statusBooking, @RequestParam(name = "userId") Long userId) {
        List<BookingDTO> lst = bookingService.getAllBooking();
        List<BookingDTO> response = lst.stream().filter(x -> x.getUser().equals(userId) && x.getStatusBooking().equals(statusBooking))
                .collect(Collectors.toList());
        return new ResponseEntity<List<BookingDTO>>(response, HttpStatus.OK);
    }

    @GetMapping("/bydate/{date}")
    public ResponseEntity<?> listBookingByDate(@PathVariable(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<BookingDTO> lst = bookingService.getByDate(date);
        return new ResponseEntity<List<BookingDTO>>(lst, HttpStatus.OK);
    }

    @GetMapping("/byuserId/{userId}")
    public ResponseEntity<?> listBookingByDate(@PathVariable(value = "userId") Long userId) {
        List<BookingDTO> lst = bookingService.getBookingByUserId(userId);
        return new ResponseEntity<List<BookingDTO>>(lst, HttpStatus.OK);
    }


    @PostMapping("/execute")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Закрытие бронирование бронирования",
            description = "Обновление бронирования"
    )
    public ResponseEntity<?> executeBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        bookingDTO = bookingService.updateBooking(bookingDTO);
        return new ResponseEntity(new ApiResponse(true, "Booking updated Successfully", bookingDTO), HttpStatus.OK);
    }

}
