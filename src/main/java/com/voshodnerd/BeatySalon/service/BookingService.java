package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.BookingRepository;
import com.voshodnerd.BeatySalon.jpa.DiscountRepository;
import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.Booking;
import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.model.StatusBooking;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.model.dto.MasterDTO;
import com.voshodnerd.BeatySalon.model.dto.UserDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final DiscountRepository discountRepository;
    @Value("${work.stattime.hour}")
    private int startHour;
    @Value("${work.endtime.hour}")
    private int endHour;

    public BookingDTO toBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setSum(booking.getSum());
        bookingDTO.setTotalSum(booking.getTotalSum());
        bookingDTO.setUser(new UserDTO(booking.getUsers().getId(), booking.getUsers().getName(), booking.getUsers().getEmail()));
        bookingDTO.setMaster(new MasterDTO(booking.getMaster().getId(), booking.getMaster().getName()));
        return bookingDTO;
    }

    public Booking createBooking(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        Optional<Users> optionalMaster = userRepository.findById(bookingDTO.getMaster().getId());
        Optional<Users> optionalUsers = userRepository.findById(bookingDTO.getUser().getId());
        booking.setDate(bookingDTO.getDate());
        booking.setMaster(optionalMaster.get());
        booking.setUsers(optionalUsers.get());
        booking.setStatus(StatusBooking.NEW);
        booking.getServiceList().addAll(booking.getServiceList());
        int sumAllServices = bookingDTO.getServiceList().stream().mapToInt(x -> x.getPrice()).sum();
        booking.setSum(sumAllServices);
        // применение скидки
        Optional<List<Discount>> optionalDiscounts = discountRepository.findByPeriodAndUser(bookingDTO.getDate(), optionalUsers.get().getId());
        if (optionalDiscounts.isPresent()) {
            Discount discount = optionalDiscounts.get().get(0);
            int sizeDiscount = Math.round(((float) discount.getValue() / (float) sumAllServices) * 100);
            booking.setTotalSum(sumAllServices - sizeDiscount);
        } else {
            booking.setTotalSum(sumAllServices);
        }
        return new Booking();
    }

    public List<BookingDTO> getBookingByMasterId(Long masterId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Optional<Users> optionalUsers = userRepository.findById(masterId);
        if (!optionalUsers.isPresent()) return bookingDTOS;
        Optional<List<Booking>> optionalBookings = repository.findByMaster(optionalUsers.get());
        if (!optionalBookings.isPresent()) return bookingDTOS;

        for (Booking x : optionalBookings.get()) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(x.getId());
            bookingDTO.setDate(x.getDate());
            bookingDTO.setSum(x.getSum());
            bookingDTO.setTotalSum(x.getTotalSum());
            Users user = userRepository.findById(x.getUsers().getId()).get();
            bookingDTO.setUser(new UserDTO(user.getId(), user.getName(), user.getEmail()));
            Users master = userRepository.findById(x.getMaster().getId()).get();
            bookingDTO.setMaster(new MasterDTO(master.getId(), master.getName()));
            bookingDTO.setServiceList(x.getServiceList());
            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;
    }

    public BookingDTO updateBooking(BookingDTO bookingDTO) {
        return bookingDTO;
    }


    public ApiResponse bookingValidation(BookingDTO bookingDTO) {
        DateTime bookingTime= new DateTime(bookingDTO.getDate());
        DateTime start = new DateTime(bookingTime.getYear(), bookingTime.getMonthOfYear(), bookingTime.getDayOfMonth(), 0, 0);
        DateTime end = new DateTime(bookingTime.getYear(), bookingTime.getMonthOfYear(), bookingTime.getDayOfMonth() + 1, 0, 0);
        Optional<Users> optionalMaster = userRepository.findById(bookingDTO.getMaster().getId());
        Optional<Users> optionalUsers = userRepository.findById(bookingDTO.getUser().getId());
        if (!optionalMaster.isPresent()) return new ApiResponse(false, MessageConstant.NO_MASTER);
        if (!optionalUsers.isPresent()) return new ApiResponse(false, MessageConstant.NO_USER);
        if (bookingDTO.getServiceList() == null || bookingDTO.getServiceList().size() == 0)
            return new ApiResponse(false, MessageConstant.NO_SERVICE_LIST);
        if (bookingTime.hourOfDay().get() < startHour)
            return new ApiResponse(false, MessageConstant.EARLIER_START_WORK_DAY);
        if (bookingTime.hourOfDay().get() > endHour)
            return new ApiResponse(false, MessageConstant.LATE_FINISH_WORK_DAY);
        int totalTimeWork = bookingDTO.getServiceList().stream().mapToInt(x -> x.getDurationInMinute()).sum();

        DateTime endTimeBooking = bookingTime.plusMinutes(totalTimeWork);
        if (endTimeBooking.getHourOfDay() > endHour)
            return new ApiResponse(false, MessageConstant.BOOKING_TIME_IS_LONG);

        Optional<List<Booking>> optionalBookings = repository.findByDateBetweenAndMaster(start, end, optionalMaster.get());
        if (!optionalBookings.isPresent())
            return new ApiResponse(true, MessageConstant.BOOKING_IS_PERMITTED, createBooking(bookingDTO));

        DateTime startTimeBooking = bookingTime;
        for (Booking item : optionalBookings.get()) {
            DateTime startTime = new DateTime(item.getDate());
            DateTime endTime = startTime.plusMinutes(item.getServiceList().stream().mapToInt(x -> x.getDurationInMinute()).sum());
            // b.start <= b.end AND a.end >= b.start condition intersects
            if (startTime.isBefore(endTimeBooking) && endTime.isAfter(startTimeBooking)) {
                return new ApiResponse(false, MessageConstant.BOOKING_TIME_INTERSECTS);
            }
        }
        return new ApiResponse(true, MessageConstant.BOOKING_IS_PERMITTED, createBooking(bookingDTO));

    }
}
