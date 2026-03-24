package org.example.ceylontraditionalmedicinecenter.controller;



import org.example.ceylontraditionalmedicinecenter.dto.PaymentDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.PaymentService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO>makePayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            boolean saved = paymentService.savePayment(paymentDTO);
            if (saved) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created,"Payment Successfull",paymentDTO));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Not_Acceptable,"Payment Failed",null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,e.getMessage(),null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllPayments() {
        try {
            List<Map<String,Object>> allPayments = paymentService.getAllPayments();
            System.out.println(allPayments);

            if (allPayments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDTO(VarList.No_Content,"No Payments found",null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.Created,"All Payments retrieved",allPayments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,e.getMessage(),null));
        }
    }
}
