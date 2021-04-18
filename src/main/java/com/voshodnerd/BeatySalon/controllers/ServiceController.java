package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.model.ConsumeMaterial;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.model.dto.ConsumeMaterialDTO;
import com.voshodnerd.BeatySalon.model.dto.ServiceItemDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.service.ManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/service")
@Slf4j
@Tag(name = "Контроллер услуг салона", description = "Методы по работе с услугами")
public class ServiceController {
    private final ManageService manageService;
    private ModelMapper modelMapper;

    @Autowired
    public ServiceController(ManageService manageService) {
        this.manageService = manageService;
        ModelMapper modelMapper = new ModelMapper();
        this.modelMapper = modelMapper;
        this.modelMapper.addConverter(serviceDtoConverter);
        log.info("ModelMapper added serviceDtoConverter");
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создание услуги салона",
            description = ""
    )
    public ResponseEntity<?> createService(@Valid @RequestBody ServiceItem service) {
        service = manageService.createService(service);
        return new ResponseEntity(new ApiResponse(true, "Service created Successfully", service), HttpStatus.OK);
    }

    @GetMapping("/all_with_consume_material")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Список всех услуг салона",
            description = "Получение всех услуг салона со списком необходимых для работы материалов"
    )
    public List<ServiceItemDTO> getAllServiceList() {
        List<ServiceItemDTO> list = manageService.getAllActiveService().stream().map(
                x -> modelMapper.map(x, ServiceItemDTO.class)
        ).collect(Collectors.toList());
        return list;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновление услуги салона",
            description = ""
    )
    public ResponseEntity<?> updateService(@Valid @RequestBody ServiceItemDTO service) {
        ServiceItem serviceItem = manageService.updateService(service);
        ServiceItemDTO dto = modelMapper.map(serviceItem, ServiceItemDTO.class);
        return new ResponseEntity(new ApiResponse(true, "Service updated Successfully", dto), HttpStatus.OK);
    }

    @GetMapping("/getbyId/{serviceId}")
    public ResponseEntity<?> getById(@PathVariable Long serviceId) {
        ServiceItem item = manageService.getById(serviceId);
        return new ResponseEntity<ServiceItemDTO>(modelMapper.map(item, ServiceItemDTO.class), HttpStatus.OK);
    }

    Converter<ConsumeMaterial, ConsumeMaterialDTO> consumeMaterialConverter = context -> {
        //This custom converter replaces the one automatically created by ModelMapper,
        //So we have to map each of the contact fields as well.
        context.getDestination().setIdMaterial(context.getSource().getMaterial().getId());
        context.getDestination().setName(context.getSource().getMaterial().getName());
        context.getDestination().setQuantity(context.getSource().getQuantity());
        return context.getDestination();
    };

    Converter<ServiceItem, ServiceItemDTO> serviceDtoConverter = context -> {
        //This custom converter replaces the one automatically created by ModelMapper,
        //So we have to map each of the contact fields as well.
        context.getDestination().setId(context.getSource().getId());
        context.getDestination().setActive(context.getSource().getActive());
        context.getDestination().setCategory(context.getSource().getCategory());
        context.getDestination().setDurationInMinute(context.getSource().getDurationInMinute());
        context.getDestination().setName(context.getSource().getName());
        context.getDestination().setPrice(context.getSource().getPrice());
        for (ConsumeMaterial material : context.getSource().getMaterialList()) {
            context.getDestination().getMaterialList().add(new ConsumeMaterialDTO(material.getMaterial().getId(), material.getMaterial().getName(), material.getQuantity().intValue()));
        }
        return context.getDestination();
    };

}
