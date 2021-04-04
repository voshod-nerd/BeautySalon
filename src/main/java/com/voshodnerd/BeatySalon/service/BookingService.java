package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.BookingRepository;
import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.Booking;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.model.dto.MasterDTO;
import com.voshodnerd.BeatySalon.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        return bookingDTO;
    }

    public List<BookingDTO> getBookingByMasterId(Long masterId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Optional<Users> optionalUsers = userRepository.findById(masterId);
        if (!optionalUsers.isPresent()) return bookingDTOS;
        Optional<List<Booking>> optionalBookings = repository.findByMaster(optionalUsers.get());
        if (!optionalBookings.isPresent()) return bookingDTOS;

        for (Booking x:optionalBookings.get()) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(x.getId());
            bookingDTO.setDate(x.getDate());
            bookingDTO.setSum(x.getSum());
            bookingDTO.setTotalSum(x.getTotalSum());
            Users user = userRepository.findById(x.getUsers().getId()).get();
            bookingDTO.setUser(new UserDTO(user.getId(),user.getName(),user.getEmail()));
            Users master = userRepository.findById(x.getMaster().getId()).get();
            bookingDTO.setMaster(new MasterDTO(master.getId(),master.getName()));
            bookingDTO.setServiceList(x.getServiceList());
            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;
    }

    public BookingDTO updateBooking(BookingDTO bookingDTO) {
        return bookingDTO;
    }

    public boolean deleteBooking(BookingDTO bookingDTO) {
        return true;
    }
}
