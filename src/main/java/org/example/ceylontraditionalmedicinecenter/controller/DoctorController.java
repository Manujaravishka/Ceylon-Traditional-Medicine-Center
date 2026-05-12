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

// Allows CORS requests from specified origins; here it permits any origin.
@CrossOrigin(origins = "*")
// Combines @Controller and @ResponseBody to expose REST endpoints returning JSON/XML.
@RestController

// Defines base URL mapping for the controller or a request mapping for a handler method.
@RequestMapping("api/v1/doctor")
public class DoctorController {
    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private DoctorService doctorService;
    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private EmailService emailService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveDoctor(
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("fullName") String fullName,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("description") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("email") String email,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl", required = false) MultipartFile image,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("linkedin") String linkedin,
            // Binds a query parameter or form field to a method parameter.
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

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/update/{email}")
    public ResponseEntity<ResponseDTO> updateDoctor(
            // Binds a URI template variable to a method parameter.
            @PathVariable String email,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editDoctorName") String fullName,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editDoctorDescription") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "editDoctorImage", required = false) MultipartFile image,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editDoctorLinkedIn") String linkedin,
            // Binds a query parameter or form field to a method parameter.
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
    // Maps HTTP PUT requests to this handler method.
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
    // Maps HTTP PUT requests to this handler method.
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

    // Maps HTTP GET requests to this handler method.
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
    // Maps HTTP GET requests to this handler method.
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

    // Maps HTTP DELETE requests to this handler method.
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

