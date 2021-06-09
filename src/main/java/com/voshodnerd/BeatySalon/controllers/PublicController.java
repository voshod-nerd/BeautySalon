package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.DiscountRepository;
import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.TypeDiscount;
import com.voshodnerd.BeatySalon.model.authentication.RoleName;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.service.BookingService;
import com.voshodnerd.BeatySalon.service.ManageService;
import com.voshodnerd.BeatySalon.utils.FileUploadUtil;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Публичный контроллер", description = "В нем расположены общедоступные методы которые не требуют авторизации")
public class PublicController {

    private final UserRepository userRepository;
    private final ManageService manageService;
    private final BookingService bookingService;
    private final DiscountRepository discountRepository;

    private final String uploadDir = "/static/images";

    @GetMapping("/master/all")
    @Operation(
            summary = "Получить всех мастеров салона",
            description = "Список всех активных мастеров салона"
    )
    public List<Users> getAllMaster() {
        List<Users> result = userRepository.findAll().stream()
                .filter(x -> x.getRole().equals(RoleName.ROLE_MASTER)).
                        filter(x -> x.getActive())
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/booking_list/{masterId}")
    public ResponseEntity<?> listBookingByMaster(@PathVariable Long masterId) {
        List<BookingDTO> lst = bookingService.getBookingByMasterId(masterId);
        return new ResponseEntity<List<BookingDTO>>(lst, HttpStatus.OK);
    }

    @GetMapping("/service/all")
    @Operation(
            summary = "Список всех услуг салона",
            description = "Получение всех услуг салон"
    )
    public List<ServiceItem> getAllServiceList() {
        return manageService.getAllActiveService();
    }


    @GetMapping("/discounts/all")
    @Operation(
            summary = "Список всех не персональных сикдов",
            description = "Список всех не персональных скидок"
    )
    public List<Discount> getAllNonPersonalDiscount() {
        return discountRepository.findByType(TypeDiscount.PROMOCOD);
    }


    @GetMapping(
            value = "/image/{image}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImage(@PathVariable String image) throws IOException {
        String userDirectory = FileSystems.getDefault()
                .getPath("")
                .toAbsolutePath()
                .toString();
        String filepath = userDirectory + uploadDir + "/" + image;
        File initialFile = new File(filepath);
        InputStream targetStream = new FileInputStream(initialFile);
        return IOUtils.toByteArray(targetStream);
    }


    @PostMapping("/upload")
    public ApiResponse savePicture(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String userDirectory = FileSystems.getDefault()
                .getPath("")
                .toAbsolutePath()
                .toString();
        if (FileUploadUtil.saveFile(userDirectory + "/" + uploadDir, fileName, multipartFile))
            return new ApiResponse(true, MessageConstant.PICTURE_SAVE_SUCC, fileName);
        else return new ApiResponse(false, MessageConstant.PICTURE_SAVE_ERROR);
    }

}
