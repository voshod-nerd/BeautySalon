package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.DiscountRepository;
import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.ConsumeMaterial;
import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.TypeDiscount;
import com.voshodnerd.BeatySalon.model.dto.ConsumeMaterialDTO;
import com.voshodnerd.BeatySalon.model.dto.DiscountDTO;
import com.voshodnerd.BeatySalon.model.dto.ServiceItemDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер скидок", description = "Методы по работе со скидками")
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    private DiscountRepository repository;
    @Autowired
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public void DiscountController() {
        ModelMapper modelMapper = new ModelMapper();
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        this.modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        this.modelMapper.addConverter(discountDtoConverter);
        log.info("ModelMapper added discountConverter");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Список всех скидок",
            description = "Список всех скидок"
    )
    @GetMapping("/all")
    public List<DiscountDTO> getAllDiscount() {
        List<DiscountDTO> list = new ArrayList<>();
        List<Discount> all = repository.findAll();
        for (Discount x : all) {
            DiscountDTO discountDTO = new DiscountDTO();
            discountDTO.setId(x.getId());
            discountDTO.setStartDate(x.getStartDate());
            discountDTO.setEndDate(x.getEndDate());
            discountDTO.setName(x.getName());
            discountDTO.setType(x.getType());
            discountDTO.setValue(x.getValue());
            discountDTO.setDescription(x.getDescription());
            discountDTO.setUsers(x.getUsers() != null ? x.getUsers().getId() : null);
            list.add(discountDTO);
        }

        return list;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создание новой скидки",
            description = "Создание новой скидки"
    )
    public ResponseEntity<?> createNewDiscount(@RequestBody DiscountDTO discount) {
        Discount discountEntity = new Discount();
        discountEntity.setDescription(discount.getDescription());
        discountEntity.setStartDate(discount.getStartDate());
        discountEntity.setEndDate(discount.getEndDate());
        discountEntity.setValue(discount.getValue());
        discountEntity.setName(discount.getName());
        discountEntity.setType(discount.getType());
        discountEntity.setDescription(discount.getDescription());
        if (discount.getUsers()!=null) {
            discountEntity.setUsers(userRepository.findById(discount.getUsers()).orElse(null));
        }
        discountEntity = repository.save(discountEntity);
        return new ResponseEntity(new ApiResponse(true, "Discount created Successfully", discountEntity), HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление  скидки",
            description = "Удаление новой скидки"
    )
    public ResponseEntity<?> deleteDiscount(@RequestBody DiscountDTO discount) {
        Optional<Discount> optional = repository.findById(discount.getId());
        if (optional.isPresent()) {
            repository.delete(optional.get());
            new ResponseEntity(new ApiResponse(true, "Discount deleted Successfully"), HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(false, "Discount deleted Unsuccessfully"), HttpStatus.OK);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновление скидки",
            description = "Обновление скидки"
    )
    public ResponseEntity<?> updateDiscount(@Valid @RequestBody Discount discount) {
        discount = repository.save(discount);
        return new ResponseEntity(new ApiResponse(true, "Discount updated Successfully", discount), HttpStatus.OK);
    }


    Converter<Discount, DiscountDTO> discountDtoConverter = context -> {
        //This custom converter replaces the one automatically created by ModelMapper,
        //So we have to map each of the contact fields as well.
        context.getDestination().setId(context.getSource().getId());
        context.getDestination().setStartDate(context.getSource().getStartDate());
        context.getDestination().setEndDate(context.getSource().getEndDate());
        context.getDestination().setType(context.getSource().getType());
        context.getDestination().setValue(context.getSource().getValue());
        context.getDestination().setDescription(context.getSource().getDescription());
        context.getDestination().setName(context.getSource().getName());
        Long value = context.getSource().getUsers() != null ? context.getSource().getUsers().getId() : null;
        context.getDestination().setUsers(value);
        return context.getDestination();
    };
}
