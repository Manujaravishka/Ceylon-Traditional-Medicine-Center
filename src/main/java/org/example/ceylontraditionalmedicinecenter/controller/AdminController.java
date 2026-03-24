package org.example.ceylontraditionalmedicinecenter.controller;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "*")  // any origin allow@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "Hello Admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String helloUser() {
        return "Hello User";
    }
}
