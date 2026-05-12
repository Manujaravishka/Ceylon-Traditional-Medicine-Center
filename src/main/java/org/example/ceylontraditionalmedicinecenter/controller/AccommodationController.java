package org.example.ceylontraditionalmedicinecenter.controller;

import org.example.ceylontraditionalmedicinecenter.dto.AccommodationDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Accommodation;
import org.example.ceylontraditionalmedicinecenter.service.AccommodationService;
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
@RequestMapping("api/v1/accommodation")
public class AccommodationController {

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private AccommodationService accommodationService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO>SaveAccommodation(
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("name") String name,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("description") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("location") String location,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("category") String category,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("costPerDay") String costPerDay,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "booked", defaultValue = "NO") String booked,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl",required = false)MultipartFile image){
        try{
            AccommodationDTO accommodationDTO = new AccommodationDTO();
            accommodationDTO.setName(name);
            accommodationDTO.setDescription(description);
            accommodationDTO.setLocation(location);
            accommodationDTO.setCategory(category);
            accommodationDTO.setCostPerDay(costPerDay);
            accommodationDTO.setBooked(booked);  // Set booked from param

            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                accommodationDTO.setImageUrl(imagePath);
            }
            int res = accommodationService.saveAccommodation(accommodationDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Accommodation Saved Successfully", accommodationDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Accommodation Already Exists", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
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
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateAccommodation(
            // Binds a URI template variable to a method parameter.
            @PathVariable Long id,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editAccommodationName") String name,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editAccommodationDescription") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editAccommodationLocation") String location,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editAccommodationCategory") String category,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("editAccommodationCostPerDay") String costPerDay,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "editAccommodationBooked", defaultValue = "NO") String booked,  // Added with default "NO"
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "editAccommodationImage", required = false) MultipartFile image) {
        System.out.println(id);
        try {

            AccommodationDTO accommodationDTO = new AccommodationDTO();
            accommodationDTO.setName(name);
            accommodationDTO.setDescription(description);
            accommodationDTO.setLocation(location);
            accommodationDTO.setCategory(category);
            accommodationDTO.setCostPerDay(costPerDay);
            accommodationDTO.setBooked(booked);  // Set booked from param

            // Handle Image Upload
            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                accommodationDTO.setImageUrl(imagePath);
            }

            int res = accommodationService.updateAccommodation(id, accommodationDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Accommodation Updated Successfully", accommodationDTO));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Accommodation Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    // Maps HTTP DELETE requests to this handler method.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteAccommodation(@PathVariable Long id) {
        try {
            int res = accommodationService.deleteAccommodation(id);
            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Accommodation Deleted Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Accommodation Not Found", null));
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
    public ResponseEntity<ResponseDTO> getAllAccommodation() {
        try {
            List<AccommodationDTO> allAccommodation = accommodationService.getAllAccommodation();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.Created, "All Accommodation Retrieved Successfully", allAccommodation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
