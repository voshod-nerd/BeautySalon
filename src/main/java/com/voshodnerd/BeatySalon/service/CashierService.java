package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.BookingRepository;
import com.voshodnerd.BeatySalon.jpa.CashierRepository;
import com.voshodnerd.BeatySalon.jpa.MaterialRepository;
import com.voshodnerd.BeatySalon.jpa.TransactionRepository;
import com.voshodnerd.BeatySalon.model.*;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CashierService {
    private final CashierRepository repository;
    private final TransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;
    private final MaterialRepository materialRepository;
    @Value("${uuid.cashier}")
    private String cashierId;

    @Transactional
    public void closeBooking(BookingDTO bookingDTO) {
        Cashier cashier = repository.findById(UUID.fromString(cashierId)).orElseThrow();
        TransactionElement transaction = new TransactionElement();
        transaction.setOperation(OperationType.INCOME);
        transaction.setSum(bookingDTO.getTotalSum());
        transaction.setTime(new Date());
        transaction.setDescription("Оплата за запись № " + bookingDTO.getId() + " : Исполнитель мастер  " + bookingDTO.getMaster().getName());
        transaction = transactionRepository.save(transaction);
        cashier.setTotalSum(cashier.getTotalSum() + transaction.getSum());
        repository.save(cashier);
    }

    @Transactional
    public void wasteMaterialOfBooking(BookingDTO bookingDTO) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingDTO.getId());
        if (!optionalBooking.isPresent()) return;
        Booking booking = optionalBooking.get();
        Set<ServiceItem> serviceList = booking.getServiceList();
        serviceList.forEach(x -> {
            List<ConsumeMaterial> consumeMaterials = x.getMaterialList();
            for (ConsumeMaterial consumeMaterial : consumeMaterials) {
                Optional<Material> optionalMaterial = materialRepository.findById(consumeMaterial.getMaterial().getId());
                if (!optionalMaterial.isPresent()) continue;
                Material material = optionalMaterial.get();
                material.setValue(material.getValue() - consumeMaterial.getQuantity());
                materialRepository.save(material);
            }
        });
    }


    @Transactional
    public void setBookingDone(List<Booking> list) {
        Cashier cashier = repository.findById(UUID.fromString(cashierId)).orElseThrow();

        float totalSumWork = list.stream().map(x -> x.getTotalSum()).reduce((float) 0, Float::sum);
        float sumForMaster = Math.round(0.6 * totalSumWork);
        for (Booking booking : list) {
            booking.setStatus(StatusBooking.DONE);
            bookingRepository.save(booking);
        }
        TransactionElement transaction = new TransactionElement();
        transaction.setOperation(OperationType.OUTCOME);
        transaction.setSum(sumForMaster);
        transaction.setTime(new Date());
        transaction.setDescription("Выплаты заработной платы " + " : Исполнитель мастер  " + list.get(0).getMaster().getName());
        transaction = transactionRepository.save(transaction);
        cashier.setTotalSum(cashier.getTotalSum() + transaction.getSum());
        repository.save(cashier);
    }
}
