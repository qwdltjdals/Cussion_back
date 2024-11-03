package org.test.teamproject_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.teamproject_back.dto.request.ReqAddReviewDto;
import org.test.teamproject_back.dto.request.ReqModifyReviewDto;
import org.test.teamproject_back.entity.Review;
import org.test.teamproject_back.repository.ReviewMapper;
import org.test.teamproject_back.repository.UserMapper;
import org.test.teamproject_back.security.principal.PrincipalUser;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class ReviewService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ReviewMapper reviewMapper;

    @Transactional(rollbackFor = Exception.class)
    public void addReview(ReqAddReviewDto dto) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        reviewMapper.addReview(dto.toReview(principalUser.getId()));
    }

    public Review getAllReviews() {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return reviewMapper.findReviewByUserId(principalUser.getId());
    }

    @Transactional(rollbackFor = SQLException.class)
    public void modifyReview(ReqModifyReviewDto dto) {
        reviewMapper.updateReview(dto.toReview());
    }

    @Transactional(rollbackFor = SQLException.class)
    public void deleteReview(int reviewId) {
        reviewMapper.deleteReview(reviewId);
    }
}