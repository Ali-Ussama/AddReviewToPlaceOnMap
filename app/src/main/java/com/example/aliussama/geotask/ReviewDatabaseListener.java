package com.example.aliussama.geotask;

import com.example.aliussama.geotask.Model.ReviewModel;

import java.util.ArrayList;

public interface ReviewDatabaseListener {

    void onAddReviewSuccess(ReviewModel review);

    void onAddReviewFailure(Exception e);

    void onReadAllReviewSuccess(ArrayList<ReviewModel> reviews);

    void onReadAllReviewFailure(Exception e);
}
