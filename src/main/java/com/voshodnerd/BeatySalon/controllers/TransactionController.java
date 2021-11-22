package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.CashierRepository;
import com.voshodnerd.BeatySalon.jpa.TransactionRepository;
import com.voshodnerd.BeatySalon.model.Cashier;
import com.voshodnerd.BeatySalon.model.Material;
import com.voshodnerd.BeatySalon.model.OperationType;
import com.voshodnerd.BeatySalon.model.TransactionElement;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
@Tag(name = "Контроллер кассы", description = "Методы по работе с кассой")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final CashierRepository cashierRepository;

    @Value("${uuid.cashier}")
    private String cashier;


    @GetMapping("/balance")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Информация о кассе",
            description = "Сколько денег в кассе"
    )
    public ResponseEntity<?> balance() {
        Optional<Cashier> optional = cashierRepository.findById(UUID.fromString(cashier));
        if (!optional.isPresent())
            return new ResponseEntity(new ApiResponse(false, MessageConstant.NOT_FOUND_CASHIER), HttpStatus.OK);
        return new ResponseEntity<>(new ApiResponse(true, MessageConstant.MONEY_BALANCE, optional.get()), HttpStatus.OK);
    }

    @GetMapping("/operation_list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Все транзакции",
            description = "Все транзакции"
    )
    public ResponseEntity<?> listTransaction() {
        List<TransactionElement> list = transactionRepository.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }


    @PostMapping("/perform")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Выполнение любых операций с кассой",
            description = "INCOME- добавление OUTCOME - выдача наличных"
    )
    public ResponseEntity<?> doTransaction(@Valid @RequestBody TransactionElement element) {
        element.setTime(new Date());
        element = transactionRepository.save(element);
        Optional<Cashier> optional = cashierRepository.findById(UUID.fromString(cashier));
        if (!optional.isPresent())
            return new ResponseEntity(new ApiResponse(false, MessageConstant.NOT_FOUND_CASHIER), HttpStatus.OK);

        Cashier cashier = optional.get();
        if (element.getOperation().equals(OperationType.INCOME)) {
            cashier.setTotalSum(cashier.getTotalSum().add(element.getSum()));
        }
        if (element.getOperation().equals(OperationType.OUTCOME)) {
            cashier.setTotalSum(cashier.getTotalSum().subtract(element.getSum()));
            if (cashier.getTotalSum().compareTo(new BigDecimal(0l)) > 0)
                return new ResponseEntity(new ApiResponse(false, MessageConstant.CASHIER_LESS_ZERO), HttpStatus.OK);
        }
        cashier = cashierRepository.save(cashier);

        return new ResponseEntity(new ApiResponse(true, MessageConstant.TRANSACTION_MONEY_PERFORMED, cashier), HttpStatus.OK);
    }
}
