package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.TreatPackageDTO;
import org.example.ceylontraditionalmedicinecenter.entity.TreatPackage;
import org.example.ceylontraditionalmedicinecenter.repository.TreatPackageRepository;
import org.example.ceylontraditionalmedicinecenter.service.TreatPackageService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreatPackageServiceImpl implements TreatPackageService {

    @Autowired
    private TreatPackageRepository treatPackageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int savePackage(TreatPackageDTO treatPackageDTO) {
        if (treatPackageRepository.existsByName(treatPackageDTO.getName())) {
            return VarList.Not_Acceptable;
        }
        TreatPackage treatPackage = modelMapper.map(treatPackageDTO, TreatPackage.class);
        treatPackageRepository.save(treatPackage);
        return VarList.Created;
    }

    @Override
    public List<TreatPackageDTO> getAllPackages() {
        List<TreatPackage> packages = treatPackageRepository.findAll();
        return packages.stream().map(p -> modelMapper.map(p, TreatPackageDTO.class)).collect(Collectors.toList());
    }

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
        treatPackageRepository.save(existing);
        return VarList.Created;
    }

    @Override
    public int deletePackage(Long id) {
        if (!treatPackageRepository.existsById(id)) {
            return VarList.Not_Found;
        }
        treatPackageRepository.deleteById(id);
        return VarList.Created;
    }
}

