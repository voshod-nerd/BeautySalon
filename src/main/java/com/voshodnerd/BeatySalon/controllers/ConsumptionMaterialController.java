package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.ConsumptionMaterialRepository;
import com.voshodnerd.BeatySalon.jpa.MaterialRepository;
import com.voshodnerd.BeatySalon.jpa.ServiceItemRepository;
import com.voshodnerd.BeatySalon.model.ConsumeMaterial;
import com.voshodnerd.BeatySalon.model.Material;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.dto.ConsumeMaterialDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
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
@RequiredArgsConstructor
@RequestMapping("/api/consume")
@Tag(name = "Бронирования контроллер", description = "Методы по работе с брониированием")
@Slf4j
public class ConsumptionMaterialController {
    private final ServiceItemRepository serviceItemRepository;
    private final MaterialRepository materialRepository;
    private final ConsumptionMaterialRepository consumptionMaterialRepository;

    private ModelMapper modelMapper;

    @PostConstruct
    public void initConsumptionMaterial() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.addConverter(consumeMaterialConverter);
        this.modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        log.info("ModelMapper added consumeMaterialConverter");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создание нового потребляемого материал",
            description = "Создание нового материала"
    )
    public ResponseEntity<?> createNewConsumeMaterial(@RequestBody ConsumeMaterialDTO consumeMaterialDTO) {
        Optional<Material> optionalMaterial = materialRepository.findById(consumeMaterialDTO.getIdMaterial());
        Optional<ServiceItem> optionalServiceItem = serviceItemRepository.findById(consumeMaterialDTO.getServiceId());
        if (optionalMaterial.isPresent() || optionalServiceItem.isPresent()) {
            ConsumeMaterial consumeMaterial = new ConsumeMaterial();
            consumeMaterial.setMaterial(optionalMaterial.get());
            consumeMaterial.setServiceItem(optionalServiceItem.get());
            consumeMaterial.setQuantity(consumeMaterialDTO.getQuantity());
            consumeMaterial = consumptionMaterialRepository.save(consumeMaterial);
            return new ResponseEntity(new ApiResponse(false, MessageConstant.CONSUME_MATERIAL_SUCCESSFUL_ADDED, consumeMaterial), HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(false, MessageConstant.CONSUME_MATERIAL_ERROR), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление потребляемого материал",
            description = "Удаление нового материала"
    )
    public ResponseEntity<?> deleteConsumeMaterial(@RequestBody ConsumeMaterialDTO consumeMaterialDTO) {
        Optional<ConsumeMaterial> optional = consumptionMaterialRepository.findById(consumeMaterialDTO.getId());
        if (optional.isPresent()) {
            consumptionMaterialRepository.delete(optional.get());
            return new ResponseEntity(new ApiResponse(false, MessageConstant.CONSUME_MATERIAL_DELETED), HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(false, MessageConstant.CONSUME_MATERIAL_ERROR), HttpStatus.OK);
    }

    @GetMapping("/byServiceId")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Материалы по serviceId",
            description = "Получение всех услуг салона со списком необходимых для работы материалов"
    )
    public List<ConsumeMaterialDTO> getConsumeListByServiceID(@RequestParam(name = "serviceId") Long id) {
        log.info("Get consume list for serviceId {}", id);
        Optional<ServiceItem> optionalItem = serviceItemRepository.findById(id);
        if (!optionalItem.isPresent()) return new ArrayList<>();
        Optional<List<ConsumeMaterial>> optionalConsumeMaterials = consumptionMaterialRepository.findByServiceItem(optionalItem.get());
        if (!optionalConsumeMaterials.isPresent()) return new ArrayList<>();
        List<ConsumeMaterial> list = optionalConsumeMaterials.get();
        List<ConsumeMaterialDTO> lst = list.stream().map(
                x -> modelMapper.map(x, ConsumeMaterialDTO.class))
                .collect(Collectors.toList());
        lst.stream().forEach(x -> x.setName(materialRepository.findById(x.getIdMaterial()).get().getName()));
        return lst;
    }

    Converter<ConsumeMaterial, ConsumeMaterialDTO> consumeMaterialConverter = context -> {
        //This custom converter replaces the one automatically created by ModelMapper,
        //So we have to map each of the contact fields as well.
        context.getDestination().setId(context.getSource().getId());
        context.getDestination().setIdMaterial(context.getSource().getMaterial().getId());
        context.getDestination().setName(context.getSource().getMaterial().getName());
        context.getDestination().setQuantity(context.getSource().getQuantity());
        context.getDestination().setServiceId(context.getSource().getServiceItem().getId());
        return context.getDestination();
    };
}
