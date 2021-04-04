package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.ServiceItemRepository;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManageService {
    private final ServiceItemRepository repository;

    public com.voshodnerd.BeatySalon.model.ServiceItem createService(com.voshodnerd.BeatySalon.model.ServiceItem service) {
        return repository.save(service);
    }

    public List<ServiceItem> getAllActiveService() {
        return repository.findAll().stream().filter(x -> x.getActive()).collect(Collectors.toList());
    }

    public com.voshodnerd.BeatySalon.model.ServiceItem updateService(com.voshodnerd.BeatySalon.model.ServiceItem service) {
        return repository.save(service);
    }

}
