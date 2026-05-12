package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.TreatPackageDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Activity;
import org.example.ceylontraditionalmedicinecenter.entity.TreatPackage;
import org.example.ceylontraditionalmedicinecenter.repository.ActivityRepository;
import org.example.ceylontraditionalmedicinecenter.repository.TreatPackageRepository;
import org.example.ceylontraditionalmedicinecenter.service.TreatPackageService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// @Service annotation used here.
@Service
public class TreatPackageServiceImpl implements TreatPackageService {

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private TreatPackageRepository treatPackageRepository;

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private ActivityRepository activityRepository;

    // Indicates this method overrides a method from a superclass or interface.
    @Override
    public int savePackage(TreatPackageDTO treatPackageDTO) {
        if (treatPackageRepository.existsByName(treatPackageDTO.getName())) {
            return VarList.Not_Acceptable;
        }

        TreatPackage treatPackage = new TreatPackage();
        treatPackage.setName(treatPackageDTO.getName());
        treatPackage.setPrice(treatPackageDTO.getPrice());
        treatPackage.setEstimateDays(treatPackageDTO.getEstimateDays());
        treatPackage.setImageUrl(treatPackageDTO.getImageUrl());
        treatPackage.setActivities(resolveActivities(treatPackageDTO.getActivities()));
        treatPackage.setSold(treatPackageDTO.getSold() != null ? treatPackageDTO.getSold() : 0);

        treatPackageRepository.save(treatPackage);
        return VarList.Created;
    }

    // Indicates this method overrides a method from a superclass or interface.
    @Override
    public List<TreatPackageDTO> getAllPackages() {
        List<TreatPackage> packages = treatPackageRepository.findAll();
        return packages.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Indicates this method overrides a method from a superclass or interface.
    @Override
    public int updatePackage(Long id, TreatPackageDTO treatPackageDTO) {
        var optional = treatPackageRepository.findById(id);
        if (optional.isEmpty()) {
            return VarList.Not_Found;
        }
        TreatPackage existing = optional.get();
        existing.setName(treatPackageDTO.getName());
        existing.setPrice(treatPackageDTO.getPrice());
        existing.setEstimateDays(treatPackageDTO.getEstimateDays());
        existing.setImageUrl(treatPackageDTO.getImageUrl());
        if (treatPackageDTO.getActivities() != null) {
            existing.setActivities(resolveActivities(treatPackageDTO.getActivities()));
        }
        if (treatPackageDTO.getSold() != null) {
            existing.setSold(treatPackageDTO.getSold());
        }
        treatPackageRepository.save(existing);
        return VarList.Created;
    }

    // Indicates this method overrides a method from a superclass or interface.
    @Override
    public int deletePackage(Long id) {
        if (!treatPackageRepository.existsById(id)) {
            return VarList.Not_Found;
        }
        treatPackageRepository.deleteById(id);
        return VarList.Created;
    }

    private List<Activity> resolveActivities(List<String> activityIds) {
        if (activityIds == null || activityIds.isEmpty()) {
            return Collections.emptyList();
        }
        return activityIds.stream()
                .map(id -> {
                    try {
                        return Long.parseLong(id);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(id -> id != null)
                .map(activityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private TreatPackageDTO toDTO(TreatPackage treatPackage) {
        TreatPackageDTO dto = new TreatPackageDTO();
        dto.setId(treatPackage.getId());
        dto.setName(treatPackage.getName());
        dto.setPrice(treatPackage.getPrice());
        dto.setEstimateDays(treatPackage.getEstimateDays());
        dto.setImageUrl(treatPackage.getImageUrl());
        dto.setActivities(treatPackage.getActivities() == null ? Collections.emptyList() :
                treatPackage.getActivities().stream().map(Activity::getName).collect(Collectors.toList()));
        dto.setSold(treatPackage.getSold());
        return dto;
    }
}

