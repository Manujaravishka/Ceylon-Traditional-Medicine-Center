package org.example.ceylontraditionalmedicinecenter.controller;



import org.example.ceylontraditionalmedicinecenter.dto.TreatPackageDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.TreatPackageService;
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
@RequestMapping("api/v1/package")
public class TreatPackageController {
    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private TreatPackageService treatPackageService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    // Maps HTTP POST requests to this handler method.
    @PostMapping("save")
    public ResponseEntity<ResponseDTO>savePackage(
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("name") String name,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("price") Double price,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("estimateDays") Integer estimateDays,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("activity") List<String> activityIds,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl",required = false)MultipartFile image
    ){
        try{
            TreatPackageDTO treatPackageDTO = new TreatPackageDTO();
            treatPackageDTO.setName(name);
            treatPackageDTO.setPrice(price);
            treatPackageDTO.setEstimateDays(estimateDays);
            treatPackageDTO.setActivities(activityIds);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                treatPackageDTO.setImageUrl(imagePath);
            }

            int res = treatPackageService.savePackage(treatPackageDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Package Saved Successfully", treatPackageDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Package Already Exists", null));
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
    // Maps HTTP GET requests to this handler method.
    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllPackages() {
        try {
            List<TreatPackageDTO> allPackages = treatPackageService.getAllPackages();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Packages retrieved successfully", allPackages));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    // ---------- UPDATE ----------

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updatePackage(
            // Binds a URI template variable to a method parameter.
            @PathVariable Long id,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("name") String name,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("price") Double price,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("estimateDays") Integer estimateDays,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("activity") List<String> activityIds,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl", required = false) MultipartFile image
    ) {
        try {
            TreatPackageDTO treatPackageDTO = new TreatPackageDTO();
            treatPackageDTO.setName(name);
            treatPackageDTO.setPrice(price);
            treatPackageDTO.setEstimateDays(estimateDays);
            treatPackageDTO.setActivities(activityIds);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                treatPackageDTO.setImageUrl(imagePath);
            }

            int res = treatPackageService.updatePackage(id, treatPackageDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Package Updated Successfully", treatPackageDTO));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Package Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // ---------- DELETE ----------
    // Maps HTTP DELETE requests to this handler method.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deletePackage(@PathVariable Long id) {
        try {
            int res = treatPackageService.deletePackage(id);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Package Deleted Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Package Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
