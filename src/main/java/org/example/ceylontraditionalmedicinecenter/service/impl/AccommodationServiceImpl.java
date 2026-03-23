package org.example.ceylontraditionalmedicinecenter.service.impl;

import jakarta.transaction.Transactional;
import org.example.ceylontraditionalmedicinecenter.dto.AccommodationDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Accommodation;
import org.example.ceylontraditionalmedicinecenter.repository.AccommodationRepository;
import org.example.ceylontraditionalmedicinecenter.service.AccommodationService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccommodationServiceImpl implements AccommodationService {
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public int saveAccommodation(AccommodationDTO accommodationDTO) {
        if (accommodationRepository.existsByName(accommodationDTO.getName())) {
            return VarList.Not_Acceptable; // Accommodation already exists
        }
        Accommodation accommodation = modelMapper.map(accommodationDTO, Accommodation.class);
        accommodationRepository.save(accommodation);
        return VarList.Created; // Successfully saved
    }

    @Override
    public int updateAccommodation(Long id, AccommodationDTO accommodationDTO) {
        Optional<Accommodation> existingAccommodationOpt = accommodationRepository.findById(id);
        if (existingAccommodationOpt.isPresent()) {
            Accommodation existingAccommodation = existingAccommodationOpt.get();

            existingAccommodation.setName(accommodationDTO.getName());
            existingAccommodation.setDescription(accommodationDTO.getDescription());
            existingAccommodation.setCostPerDay(accommodationDTO.getCostPerDay());
            existingAccommodation.setLocation(accommodationDTO.getLocation());
            existingAccommodation.setCategory(accommodationDTO.getCategory());

            if (accommodationDTO.getImageUrl() != null) {
                existingAccommodation.setImageUrl(accommodationDTO.getImageUrl());
            }

            accommodationRepository.save(existingAccommodation);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }
    @Transactional
    @Override
    public int deleteAccommodation(Long id) {
        if (accommodationRepository.existsById(id)) {
            accommodationRepository.deleteById(id);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }

    @Override
    public List<AccommodationDTO> getAllAccommodation() {
        List<Accommodation> accommodations = accommodationRepository.findAll();
        return accommodations.stream()
                .map(accommodation -> modelMapper.map(accommodation, AccommodationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalAccommodationCount() {
        return (int) accommodationRepository.count();
    }
}
