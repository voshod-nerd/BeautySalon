package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.MaterialRepository;
import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.model.Material;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/material")
@Tag(name = "Контроллер по работе с материалами", description = "Методы по учету материалов")
public class MaterialController {
    private final MaterialRepository materialRepository;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Список всех материалов",
            description = ""
    )
    public List<Material> getListMaterials(@Valid @RequestBody Material material) {
        List<Material> materialList = materialRepository.findAll();
        return materialList;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создание нового материала",
            description = "Создание нового материала"
    )
    public ResponseEntity<?> createNewMaterial(@Valid @RequestBody Material material) {
        Optional<Material> optionalMaterial = materialRepository.findByName(material.getName());
        if (optionalMaterial.isPresent())
            return new ResponseEntity(new ApiResponse(false, MessageConstant.MATERIAL_ALREADY_EXIST), HttpStatus.OK);
        material = materialRepository.save(material);
        return new ResponseEntity(new ApiResponse(true, MessageConstant.MATERIAL_SUCCESSFUL_CREATED, material), HttpStatus.OK);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновление материала",
            description = ""
    )
    public ResponseEntity<Material> updateMaterial(@Valid @RequestBody Material material) {
        material = materialRepository.save(material);
        return ResponseEntity.ok().body(material);
    }

}
