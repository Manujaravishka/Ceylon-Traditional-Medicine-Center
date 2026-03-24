package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.ReviewDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Review;
import org.example.ceylontraditionalmedicinecenter.repository.ReviewRepository;
import org.example.ceylontraditionalmedicinecenter.service.ReviewService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int saveReview(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        reviewRepository.save(review);
        return VarList.Created;
    }

    @Override
    public Object getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(r -> modelMapper.map(r, ReviewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public int deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            return VarList.Not_Found;
        }
        reviewRepository.deleteById(id);
        return VarList.Created;
    }
}

