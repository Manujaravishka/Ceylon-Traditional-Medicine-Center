package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "package")
public class TreatPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private Integer estimateDays;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "package_activity",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<Activity> activities;

    private String imageUrl;

}
