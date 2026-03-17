package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class TreatPackageDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer estimateDays;
    private List<String> activities;
    private String imageUrl;
}