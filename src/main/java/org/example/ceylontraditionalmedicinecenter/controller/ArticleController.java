package org.example.ceylontraditionalmedicinecenter.controller;


import org.example.ceylontraditionalmedicinecenter.dto.ArticleDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.ArticleService;
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
@RequestMapping("api/v1/article")
public class ArticleController {
    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private ArticleService articleService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveArticle(
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("title") String title,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("description") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl", required = false) MultipartFile image) {
        try {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setTitle(title);
            articleDTO.setDescription(description);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                articleDTO.setImageUrl(imagePath);
            }

            int res = articleService.saveArticle(articleDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Article Saved Successfully", articleDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Article Already Exists", null));
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
    public ResponseEntity<ResponseDTO> updateArticle(
            // Binds a URI template variable to a method parameter.
            @PathVariable Long id,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("title") String title,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam("description") String description,
            // Binds a query parameter or form field to a method parameter.
            @RequestParam(value = "imageUrl", required = false) MultipartFile image) {
        try {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setTitle(title);
            articleDTO.setDescription(description);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                articleDTO.setImageUrl(imagePath);
            }

            int res = articleService.updateArticle(id, articleDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Article Updated Successfully", articleDTO));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Article Not Found", null));
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
    public ResponseEntity<ResponseDTO> deleteArticle(@PathVariable Long id) {
        try {
            int res = articleService.deleteArticle(id);
            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Article Deleted Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Article Not Found", null));
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
    public ResponseEntity<ResponseDTO> getAllArticles() {
        try {
            List<ArticleDTO> allArticles = articleService.getAllArticles();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.Created, "All Articles Retrieved Successfully", allArticles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
