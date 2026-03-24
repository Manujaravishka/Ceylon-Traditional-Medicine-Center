package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.DoctorDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Doctor;
import org.example.ceylontraditionalmedicinecenter.repository.DoctorRepository;
import org.example.ceylontraditionalmedicinecenter.service.DoctorService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int saveDoctor(DoctorDTO doctorDTO) {
        if (doctorRepository.existsByEmail(doctorDTO.getEmail())) {
            return VarList.Not_Acceptable;
        }
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        doctorRepository.save(doctor);
        return VarList.Created;
    }

    @Override
    public int updateDoctor(String email, DoctorDTO doctorDTO) {
        Doctor doctor = doctorRepository.findByEmail(email).orElse(null);
        if (doctor == null) {
            return VarList.Not_Found;
        }
        doctor.setFullName(doctorDTO.getFullName());
        doctor.setDescription(doctorDTO.getDescription());
        doctor.setImageUrl(doctorDTO.getImageUrl());
        doctor.setLinkedin(doctorDTO.getLinkedin());
        doctor.setPaymentPerDay(doctorDTO.getPaymentPerDay());
        doctorRepository.save(doctor);
        return VarList.Created;
    }

    @Override
    public int deactivateDoctor(String email) {
        Doctor doctor = doctorRepository.findByEmail(email).orElse(null);
        if (doctor == null) {
            return VarList.Not_Found;
        }
        doctor.setStatus("INACTIVE");
        doctorRepository.save(doctor);
        return VarList.Created;
    }

    @Override
    public int activateDoctor(String email) {
        Doctor doctor = doctorRepository.findByEmail(email).orElse(null);
        if (doctor == null) {
            return VarList.Not_Found;
        }
        doctor.setStatus("ACTIVE");
        doctorRepository.save(doctor);
        return VarList.Created;
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(d -> modelMapper.map(d, DoctorDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDTO> getAvailableDoctors() {
        List<Doctor> doctors = doctorRepository.findAllByBookedAndStatus("No", "ACTIVE");
        return doctors.stream().map(d -> modelMapper.map(d, DoctorDTO.class)).collect(Collectors.toList());
    }

    @Override
    public int deleteDoctor(String email) {
        Doctor doctor = doctorRepository.findByEmail(email).orElse(null);
        if (doctor == null) {
            return VarList.Not_Found;
        }
        doctorRepository.delete(doctor);
        return VarList.Created;
    }

    @Override
    public int getTotalDoctorCount() {
        return (int) doctorRepository.count();
    }
}

