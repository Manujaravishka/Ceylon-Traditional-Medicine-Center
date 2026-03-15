package org.example.ceylontraditionalmedicinecenter.service;

import jakarta.validation.Valid;
import org.example.ceylontraditionalmedicinecenter.dto.ReviewDTO;

public interface ReviewService {
    int saveReview(@Valid ReviewDTO reviewDTO);

    Object getAllReviews();

    int deleteReview(Long id);
}
