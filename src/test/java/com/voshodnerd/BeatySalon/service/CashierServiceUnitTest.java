package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.BookingRepository;
import com.voshodnerd.BeatySalon.jpa.CashierRepository;
import com.voshodnerd.BeatySalon.jpa.MaterialRepository;
import com.voshodnerd.BeatySalon.jpa.TransactionRepository;
import com.voshodnerd.BeatySalon.model.Booking;
import com.voshodnerd.BeatySalon.model.ConsumeMaterial;
import com.voshodnerd.BeatySalon.model.Material;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CashierServiceUnitTest {
    @MockBean
    BookingRepository mockBookingRepository;
    @MockBean
    MaterialRepository mockMaterialRepository;
    @MockBean
    CashierRepository mockCashierRepository;
    @MockBean
    TransactionRepository transactionRepository;
    CashierService cashierService;
    @Spy
    Material material = new Material();


    @BeforeEach
    public void init() {
        Booking booking = new Booking();
        Set<ServiceItem> serviceItems = new HashSet<>();


        List<ConsumeMaterial> list = new ArrayList<>();

        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setMaterialList(list);
        serviceItems.add(serviceItem);
        booking.setServiceList(serviceItems);

        ConsumeMaterial consumeMaterial = new ConsumeMaterial();
        consumeMaterial.setMaterial(material);
        consumeMaterial.setQuantity(400);
        list.add(consumeMaterial);

        material.setId(1l);
        material.setValue(400);

        when(mockBookingRepository.findById(1l)).thenReturn(Optional.of(booking));
        when(mockMaterialRepository.findById(1l)).thenReturn(Optional.of(material));
        CashierService cashierService = new CashierService(mockCashierRepository, transactionRepository, mockBookingRepository, mockMaterialRepository);
        this.cashierService = cashierService;

    }

    @Test
    @DisplayName("Check out waste material ")
    public void testWasteMaterialOfBooking() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1l);
        cashierService.wasteMaterialOfBooking(bookingDTO);
        assertEquals(0, material.getValue());
    }


}