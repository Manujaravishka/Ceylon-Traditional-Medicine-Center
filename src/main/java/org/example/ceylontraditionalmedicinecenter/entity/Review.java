package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="review" )
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail; // Review එක ලබා දුන් user ගේ email

    @Column(nullable = false)
    private String comment; // Review content

    @Column(nullable = false)
    private int rating; // Rating (e.g., 1-5)

    @Column(nullable = false)
    private String createdAt; // Review එක ලබා දුන් date/time
}
