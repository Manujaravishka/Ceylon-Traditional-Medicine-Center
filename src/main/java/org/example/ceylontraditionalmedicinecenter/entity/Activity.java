package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    private String imageUrl;

    private String CostPerDay;
}
