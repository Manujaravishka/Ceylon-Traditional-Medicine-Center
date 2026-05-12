package org.example.ceylontraditionalmedicinecenter.controller;

import org.example.ceylontraditionalmedicinecenter.dto.AccommodationDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ActivityDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.ActivityService;
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
@RequestMapping("api/v1/activity")
public class ActivityController {
    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private ActivityService activityService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO>saveActivity(
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("name") String name,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("description") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("costPerDay") String costPerDay,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl",required = false)MultipartFile image){
        try{
            ActivityDTO activityDTO = new ActivityDTO();
            activityDTO.setName(name);
            activityDTO.setDescription(description);
            activityDTO.setCostPerDay(costPerDay);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                activityDTO.setImageUrl(imagePath);
            }
            int res = activityService.saveActivity(activityDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Activity Saved Successfully", activityDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Activity Already Exists", null));
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
    public ResponseEntity<ResponseDTO> updateActivity(
            // Binds a URI template variable to a method parameter.
            @PathVariable Long id,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("name") String name,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("description") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("costPerDay") String costPerDay,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl", required = false) MultipartFile image) {
        System.out.println(id);
        try {

            ActivityDTO activityDTO = new ActivityDTO();
            activityDTO.setName(name);
            activityDTO.setDescription(description);
            activityDTO.setCostPerDay(costPerDay);

            // Handle Image Upload
            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                activityDTO.setImageUrl(imagePath);
            }

            int res = activityService.updateActivity(id, activityDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Activity Updated Successfully", activityDTO));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Activity Not Found", null));
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
    public ResponseEntity<ResponseDTO> deleteActivity(@PathVariable Long id) {
        try {
            int res = activityService.deleteActivity(id);
            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Activity Deleted Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Activity Not Found", null));
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
    public ResponseEntity<ResponseDTO> getAllActivity() {
        try {
            List<ActivityDTO> allActivity = activityService.getAllActivity();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.Created, "All Activity Retrieved Successfully", allActivity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
