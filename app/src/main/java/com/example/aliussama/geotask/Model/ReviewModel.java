package com.example.aliussama.geotask.Model;

import java.util.UUID;

public class ReviewModel {

    private String mUid;
    private String mRatingStarsCount;
    private String mLatitude;
    private String mLongtitude;
    private String mReviewContent;
    private String mPlaceName;

    public ReviewModel() {
    }

    public ReviewModel(String mRatingStarsCount, String mLatitude, String mLongtitude, String mReviewContent, String mPlaceName) {
        this.mUid = UUID.randomUUID().toString();
        this.mRatingStarsCount = mRatingStarsCount;
        this.mLatitude = mLatitude;
        this.mLongtitude = mLongtitude;
        this.mReviewContent = mReviewContent;
        this.mPlaceName = mPlaceName;
    }

    public ReviewModel(String mUid, String mRatingStarsCount, String mLatitude, String mLongtitude, String mReviewContent, String mPlaceName) {
        this.mUid = mUid;
        this.mRatingStarsCount = mRatingStarsCount;
        this.mLatitude = mLatitude;
        this.mLongtitude = mLongtitude;
        this.mReviewContent = mReviewContent;
        this.mPlaceName = mPlaceName;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmRatingStarsCount() {
        return mRatingStarsCount;
    }

    public void setmRatingStarsCount(String mRatingStarsCount) {
        this.mRatingStarsCount = mRatingStarsCount;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLongtitude() {
        return mLongtitude;
    }

    public void setmLongtitude(String mLongtitude) {
        this.mLongtitude = mLongtitude;
    }

    public String getmReviewContent() {
        return mReviewContent;
    }

    public void setmReviewContent(String mReviewContent) {
        this.mReviewContent = mReviewContent;
    }

    public String getmPlaceName() {
        return mPlaceName;
    }

    public void setmPlaceName(String mPlaceName) {
        this.mPlaceName = mPlaceName;
    }
}
