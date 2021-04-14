package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.MaterialRepository;
import com.voshodnerd.BeatySalon.jpa.ServiceItemRepository;
import com.voshodnerd.BeatySalon.model.ConsumeMaterial;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.dto.ConsumeMaterialDTO;
import com.voshodnerd.BeatySalon.model.dto.ServiceItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManageService {
    private final ServiceItemRepository repository;
    private final MaterialRepository materialRepository;

    public com.voshodnerd.BeatySalon.model.ServiceItem createService(com.voshodnerd.BeatySalon.model.ServiceItem service) {
        return repository.save(service);
    }

    public ServiceItem getById(Long id) {
        return repository.findById(id).orElseThrow(null);
    }

    public List<ServiceItem> getAllActiveService() {
        return repository.findAll().stream().filter(x -> x.getActive()).collect(Collectors.toList());
    }

    public ServiceItem updateService(ServiceItemDTO service) {
        Optional<ServiceItem> optional = repository.findById(service.getId());
        if (!optional.isPresent()) throw new NoSuchElementException();
        ServiceItem item = optional.get();
        item.setName(service.getName());
        item.setCategory(service.getCategory());
        item.setPrice(service.getPrice());
        item.setDurationInMinute(service.getDurationInMinute());
        item.setActive(service.getActive());
        item.getMaterialList().removeAll(item.getMaterialList());
        item = repository.save(item);
        for (ConsumeMaterialDTO el: service.getMaterialList()) {
            ConsumeMaterial material=new ConsumeMaterial();
            material.setServiceItem(item);
            material.setQuantity(el.getQuantity());
            material.setMaterial(materialRepository.findById(el.getIdMaterial()).get());
            item.getMaterialList().add(material);
        }
        return repository.save(item);
    }

}
