package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.*;
import com.voshodnerd.BeatySalon.model.*;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.model.dto.MasterDTO;
import com.voshodnerd.BeatySalon.model.dto.UserDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final DiscountRepository discountRepository;
    private final CashierRepository cashierRepository;
    private final TransactionRepository transactionRepository;
    @Value("${work.starttime.hour}")
    private int startHour;
    @Value("${work.endtime.hour}")
    private int endHour;

    @Value("${uuid.cashier}")
    private String cashier;

    public BookingDTO toBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDateE(booking.getDateB());
        bookingDTO.setDateE(booking.getDateE());
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
        booking.setDateB(bookingDTO.getDateB());
        int duration = bookingDTO.getServiceList().stream().map(x -> x.getDurationInMinute()).reduce(0, Integer::sum);
        booking.setDateE(new Date(bookingDTO.getDateB().getTime() + (long) 60000 * duration));
        booking.setMaster(optionalMaster.get());
        booking.setUsers(optionalUsers.get());
        booking.setStatus(StatusBooking.NEW);
        booking.setServiceList(bookingDTO.getServiceList());
        float sumAllServices = bookingDTO.getServiceList().stream().mapToInt(x -> x.getPrice()).sum();
        booking.setSum(sumAllServices);
        // применение скидки
        Optional<List<Discount>> optionalDiscounts = discountRepository.findByPeriodAndUser(bookingDTO.getDateB(), bookingDTO.getDateB(), optionalUsers.get());
        if (optionalDiscounts.isPresent() && optionalDiscounts.get().size() > 0) {
            Discount discount = optionalDiscounts.get().get(0);
            int sizeDiscount = Math.round(((float) discount.getValue() / (float) sumAllServices) * 100);
            booking.setTotalSum(sumAllServices - sizeDiscount);
        } else {
            booking.setTotalSum(sumAllServices);
        }
        return booking;
    }

    public List<BookingDTO> getByDate(LocalDate date) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Optional<List<Booking>> optionalBookings = repository.findByDateBBetween(date.toDate(), date.plusDays(1).toDate());
        if (!optionalBookings.isPresent()) return bookingDTOS;
        for (Booking x : optionalBookings.get()) {
            if (x.getStatus().equals(StatusBooking.PAYED)) continue;
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(x.getId());
            bookingDTO.setDateB(x.getDateB());
            bookingDTO.setRate(x.getRate());
            bookingDTO.setDateE(x.getDateE());
            bookingDTO.setSum(x.getSum());
            bookingDTO.setTotalSum(x.getTotalSum());
            bookingDTO.setStatusBooking(x.getStatus());
            Users user = userRepository.findById(x.getUsers().getId()).get();
            bookingDTO.setUser(new UserDTO(user.getId(), user.getName(), user.getEmail()));
            Users master = userRepository.findById(x.getMaster().getId()).get();
            bookingDTO.setMaster(new MasterDTO(master.getId(), master.getName()));
            bookingDTO.setServiceList(x.getServiceList());
            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;
    }

    public List<BookingDTO> getAllBooking() {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Optional<List<Booking>> optionalBookings = Optional.of(repository.findAll());
        if (!optionalBookings.isPresent()) return bookingDTOS;
        for (Booking x : optionalBookings.get()) {
            //if (x.getStatus().equals(StatusBooking)) continue;
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(x.getId());
            bookingDTO.setDateB(x.getDateB());
            bookingDTO.setDateE(x.getDateE());
            bookingDTO.setRate(x.getRate());
            bookingDTO.setSum(x.getSum());
            bookingDTO.setTotalSum(x.getTotalSum());
            bookingDTO.setStatusBooking(x.getStatus());
            Users user = userRepository.findById(x.getUsers().getId()).get();
            bookingDTO.setUser(new UserDTO(user.getId(), user.getName(), user.getEmail()));
            Users master = userRepository.findById(x.getMaster().getId()).get();
            bookingDTO.setMaster(new MasterDTO(master.getId(), master.getName()));
            bookingDTO.setServiceList(x.getServiceList());
            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;

    }

    public List<BookingDTO> getBookingByUserId(Long userId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Optional<Users> optionalUsers = userRepository.findById(userId);
        if (!optionalUsers.isPresent()) return bookingDTOS;
        Optional<List<Booking>> optionalBookings = repository.findByUsers(optionalUsers.get());
        if (!optionalBookings.isPresent()) return bookingDTOS;

        for (Booking x : optionalBookings.get()) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(x.getId());
            bookingDTO.setDateB(x.getDateB());
            bookingDTO.setDateE(x.getDateE());
            bookingDTO.setSum(x.getSum());
            bookingDTO.setRate(x.getRate());
            bookingDTO.setStatusBooking(x.getStatus());
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

    public List<BookingDTO> getBookingByMasterId(Long masterId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Optional<Users> optionalUsers = userRepository.findById(masterId);
        if (!optionalUsers.isPresent()) return bookingDTOS;
        Optional<List<Booking>> optionalBookings = repository.findByMaster(optionalUsers.get());
        if (!optionalBookings.isPresent()) return bookingDTOS;

        for (Booking x : optionalBookings.get()) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(x.getId());
            bookingDTO.setDateB(x.getDateB());
            bookingDTO.setDateE(x.getDateE());
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
        Optional<Booking> optional = repository.findById(bookingDTO.getId());
        if (!optional.isPresent()) return null;
        Booking booking = optional.get();
        booking.setRate(bookingDTO.getRate());
        booking.setServiceList(bookingDTO.getServiceList());
        booking.setDateB(bookingDTO.getDateB());
        booking.setDateE(bookingDTO.getDateE());
        booking.setStatus(bookingDTO.getStatusBooking());
        booking.setSum(bookingDTO.getSum());
        booking.setTotalSum(bookingDTO.getTotalSum());

        if (booking.getStatus().equals(StatusBooking.DONE)) {

        }

        return toBookingDTO(repository.save(booking));
    }

    public ApiResponse bookingValidation(BookingDTO bookingDTO) {
        DateTime bookingTime = new DateTime(bookingDTO.getDateB());
        DateTime start = new DateTime(bookingTime.getYear(), bookingTime.getMonthOfYear(), bookingTime.getDayOfMonth(), 0, 0);
        DateTime end = bookingTime.plusDays(1);
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

        Optional<List<Booking>> optionalBookings = repository.findByDateBBetweenAndMaster(start.toDate(), end.toDate(), optionalMaster.get());
        if (!optionalBookings.isPresent())
            return new ApiResponse(true, MessageConstant.BOOKING_IS_PERMITTED, createBooking(bookingDTO));

        DateTime startTimeBooking = bookingTime;
        for (Booking item : optionalBookings.get()) {
            DateTime startTime = new DateTime(item.getDateB());
            DateTime endTime = startTime.plusMinutes(item.getServiceList().stream().mapToInt(x -> x.getDurationInMinute()).sum());
            // b.start <= b.end AND a.end >= b.start condition intersects
            if (startTime.isBefore(endTimeBooking) && endTime.isAfter(startTimeBooking)) {
                return new ApiResponse(false, MessageConstant.BOOKING_TIME_INTERSECTS);
            }
        }
        return new ApiResponse(true, MessageConstant.BOOKING_IS_PERMITTED, createBooking(bookingDTO));

    }

    public ApiResponse executeBooking(BookingDTO bookingDTO) {
        Optional<Booking> optional = repository.findById(bookingDTO.getId());
        if (!optional.isPresent()) return new ApiResponse(false, MessageConstant.BOOKING_NOT_FOUND);
        Booking booking = optional.get();
        booking.setStatus(StatusBooking.DONE);
        bookingDTO.setStatusBooking(StatusBooking.DONE);
        try {
            setDiscount(booking, bookingDTO);
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, MessageConstant.PROMOCODE_NOT_FOUND);
        }
        TransactionElement transaction = new TransactionElement();
        transaction.setSum(booking.getTotalSum());
        transaction.setOperation(OperationType.INCOME);
        transaction.setTime(new Date());
        transaction.setDescription(MessageConstant.INCOME_BY_CLOSE_BOOKING);
        Optional<Cashier> optionalCashier = cashierRepository.findById(UUID.fromString(cashier));
        if (!optionalCashier.isPresent()) return new ApiResponse(false, MessageConstant.NOT_FOUND_CASHIER);
        Cashier cashier = optionalCashier.get();
        transaction = transactionRepository.save(transaction);
        cashier.setTotalSum(cashier.getTotalSum() + transaction.getSum());
        cashierRepository.save(cashier);
        repository.save(booking);
        return new ApiResponse(true, MessageConstant.BOOKING_IS_EXECUTED, bookingDTO);
    }

    private void setDiscount(Booking booking, BookingDTO bookingDTO) {
        if (bookingDTO.getPromoCode() == null) return;
        List<Discount> discounts = discountRepository.findByType(TypeDiscount.valueOf(bookingDTO.getPromoCode()));
        if (discounts == null) return;
        Discount discount = discounts.get(0);
        float totalsum = booking.getTotalSum();
        totalsum = totalsum - totalsum / 100 * discount.getValue();
        bookingDTO.setTotalSum(totalsum);
        booking.setTotalSum(totalsum);
    }


}
