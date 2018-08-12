package com.example.aliussama.geotask.Model;

import android.support.annotation.NonNull;

import com.example.aliussama.geotask.ReviewDatabaseListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ReviewsDatabase {

    /**
     * Add user's review to Firebase Database then send callback
     * to MapsActivity to inform the user that the review is added
     * successfully
     *
     * @param callback is ReviewDatabaseListener Callback reference
     *                 of MapsActivity
     * @param review   is the added review via the user
     */
    public void addReview(final ReviewModel review, final ReviewDatabaseListener callback) {
        try {

            //declaring firebase database reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            //adding review to firebase database at child reviews
            reference.child("reviews").child(review.getmUid()).setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //if task is successful and complete
                    if (task.isSuccessful() && task.isComplete()) {

                        //send callback to Maps Activity with added review
                        callback.onAddReviewSuccess(review);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //send callback to Maps Activity with Exception
                    callback.onAddReviewFailure(e);
                }
            });
        } catch (Exception e) {
            //send callback to Maps Activity with Exception
            callback.onAddReviewFailure(e);
        }
    }

    /**
     * This Method Read All Reviews From Firebase Database
     *
     * @param callback is ReviewDatabaseListener Callback reference
     *                 of MapsActivity
     */
    public void readAllReviews(final ReviewDatabaseListener callback) {
        try {
            //declaring firebase database reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            //Reading All reviews from firebase database at child reviews
            reference.child("reviews").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        //declaring arrayList of ReviewModel
                        ArrayList<ReviewModel> reviews = new ArrayList<>();

                        //receiving result from Firebase database as Map
                        Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                        //if returned result is not null
                        if (result != null) {
                            //Iterate over the result to extract reviews
                            for (String key : result.keySet()) {
                                reviews.add(new ReviewModel(result.get(key).get("mUid"),
                                        result.get(key).get("mRatingStarsCount"),
                                        result.get(key).get("mLatitude"),
                                        result.get(key).get("mLongtitude"),
                                        result.get(key).get("mReviewContent"),
                                        result.get(key).get("mPlaceName")));
                            }
                            //send callback to Maps Activity with All Reviews
                            callback.onReadAllReviewSuccess(reviews);
                        }
                    } catch (Exception e) {
                        //send callback to Maps Activity with Exception
                        callback.onReadAllReviewFailure(e);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //send callback to Maps Activity with Exception
                    callback.onReadAllReviewFailure(databaseError.toException());

                }
            });
        } catch (Exception e) {
            //send callback to Maps Activity with Exception
            callback.onReadAllReviewFailure(e);
        }

    }
}
