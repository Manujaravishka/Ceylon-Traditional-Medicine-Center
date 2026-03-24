package org.example.ceylontraditionalmedicinecenter.controller;


import org.example.ceylontraditionalmedicinecenter.dto.AuthResponseDTO;
import org.example.ceylontraditionalmedicinecenter.dto.DoctorDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.DoctorService;
import org.example.ceylontraditionalmedicinecenter.service.EmailService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController

@RequestMapping("api/v1/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private EmailService emailService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveDoctor(
            @RequestParam("fullName") String fullName,
            @RequestParam("description") String description,
            @RequestParam("email") String email,
            @RequestParam(value = "imageUrl", required = false) MultipartFile image,
            @RequestParam("linkedin") String linkedin,
            @RequestParam("paymentPerDay") String paymentPerDay ){
        try{
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setFullName(fullName);
            doctorDTO.setDescription(description);
            doctorDTO.setEmail(email);
            doctorDTO.setLinkedin(linkedin);
            doctorDTO.setPaymentPerDay(paymentPerDay);
            doctorDTO.setStatus("ACTIVE");
            doctorDTO.setBooked("No");

            // Handle doctor Image Upload

            if(image != null && !image.isEmpty()){
                String imagePath = saveFile(image);
                doctorDTO.setImageUrl(imagePath);
            }

            int res = doctorService.saveDoctor(doctorDTO);

            switch (res) {
                case VarList.Created:
                    emailService.sendDoctorRegistrationEmail(email, fullName);   // ✅ Send email after successful registration
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Doctor Saved Successfully", doctorDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Doctor Already Exists", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    private String saveFile(MultipartFile file)throws IOException {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @PostMapping("/update/{email}")
    public ResponseEntity<ResponseDTO> updateDoctor(
            @PathVariable String email,
            @RequestParam("editDoctorName") String fullName,
            @RequestParam("editDoctorDescription") String description,
            @RequestParam(value = "editDoctorImage", required = false) MultipartFile image,
            @RequestParam("editDoctorLinkedIn") String linkedin,
            @RequestParam("editDoctorPayment") String paymentPerDay) {

        try {
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setFullName(fullName);
            doctorDTO.setDescription(description);
            doctorDTO.setLinkedin(linkedin);
            doctorDTO.setPaymentPerDay(paymentPerDay);
            // Handle doctor Image Upload
            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                doctorDTO.setImageUrl(imagePath);
            }

            int res = doctorService.updateDoctor(email, doctorDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Doctor Updated Successfully", doctorDTO));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Doctor Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @PutMapping("/deactivate/{email}")
    public ResponseEntity<ResponseDTO> deactivateDoctor(@PathVariable String email) {
        try {
            int res = doctorService.deactivateDoctor(email);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Doctor Deactivated Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Doctor Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @PutMapping("/active/{email}")
    public ResponseEntity<ResponseDTO> activateDoctor(@PathVariable String email) {
        try {
            int res = doctorService.activateDoctor(email);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Doctor Activated Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Doctor Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllDoctors() {
        try {
            List<DoctorDTO> allDoctors = doctorService.getAllDoctors();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.Created, "All Doctors Retrieved Successfully", allDoctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @GetMapping("/available")
    public ResponseEntity<ResponseDTO> getAvailableDoctors() {
        try {
            List<DoctorDTO> availableGuides = doctorService.getAvailableDoctors();
            if (availableGuides.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.Not_Found, "No available doctors at the moment", null));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.Created, "Available doctors Retrieved Successfully", availableGuides));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<ResponseDTO> deleteDoctor(@PathVariable String email) {
        try {
            int res = doctorService.deleteDoctor(email); // Ensure this method exists in GuideService
            if (res == VarList.Created) {
                return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Doctor Deleted Successfully", null));
            } else if (res == VarList.Not_Found) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Doctor Not Found", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}

