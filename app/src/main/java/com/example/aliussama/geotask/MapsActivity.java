package com.example.aliussama.geotask;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aliussama.geotask.Model.GpsTracker;
import com.example.aliussama.geotask.Model.ReviewModel;
import com.example.aliussama.geotask.Model.ReviewsDatabase;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationSource.OnLocationChangedListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener,
        ReviewDatabaseListener {

    // declaring google map var
    private GoogleMap mMap;

    // declaring GpsTracker reference
    private GpsTracker mGpsTracker;

    //declare Rating Stars Count var
    private int ratingStarsCount = 0;

    //declare var to hold Picked Place Name via user
    private String pickedPlaceName;

    //declare Reviews Database reference
    private ReviewsDatabase mReviewsDatabase;

    //declare ArrayList to hold submitted reviews via user
    private ArrayList<ReviewModel> submittedReviews;

    //declare var to hold Picked Place Location
    private LatLng pickedPlaceLocation;

    //declare ArrayList of MarkerOptions to hold picked places markers
    private ArrayList<MarkerOptions> mMarkerOptions;

    //declare current location var
    private Location mCurrentLocation;

    //declare Place Pick Builder request code var
    private int PLACE_PICKER_REQUEST = 1;

    //declare Place Pick Builder reference
    private PlacePicker.IntentBuilder builder;

    //declare review Dialog var
    private Dialog mReviewDialog;

    //declare array of ImageView to hold 5 rating stars
    private ImageView[] stars = new ImageView[5];

    //declare review comment EditText var
    private EditText reviewContentEditText;

    //declare var to check network state
    private ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            //initializing activity vars and UI components
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing activity vars and UI components
     */
    private void init() {
        try {
            //declare database reference
            mReviewsDatabase = new ReviewsDatabase();

            //declare arrayList of submitted reviews
            submittedReviews = new ArrayList<>();

            //declare arrayList of markers to hold submitted reviews places markers
            mMarkerOptions = new ArrayList<>();

            //declare reference from GpsTracker class
            //this class responsible for getting gps location
            mGpsTracker = new GpsTracker(new WeakReference<Context>(this));

            //declaring place builder to pick specific place
            builder = new PlacePicker.IntentBuilder();

            //declaring var to hold network state
            conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            //preparing rating dialog
            prepareDialog("");

            //declaring the Map
            setUpMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method receives returned Place from Place Picker Builder Class
     *
     * @param requestCode is the code of Place Picker Builder
     * @param resultCode  is -1 which indicate to RESULT_OK
     * @param data        which holds the selected place via user
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // handle result of place picker builder
            // if request code equals PLACE_PICKER_REQUEST equals 1
            if (requestCode == PLACE_PICKER_REQUEST) {

                if (resultCode == RESULT_OK) {
                    //getting picked place from the returned Intent
                    Place place = PlacePicker.getPlace(data, this);

                    //if place in not null
                    if (place != null) {
                        //handle returned place
                        onPlaceSelected(place);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Declaring and Initializing The Map
     */
    private void setUpMap() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            // if GPS is not accessed
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)) {
                Log.i("onMapReady", "Gps is disabled");
                // Show rationale and request permission.
                // request from user to open gps
                mGpsTracker.requestOpenGps();
            }

            Log.i("onMapReady", "Gps is enabled");
            //enable user location pointer on the map
            mMap.setMyLocationEnabled(true);

            //Getting the user GPS location
            mCurrentLocation = mGpsTracker.getLocation();

            //Converting current location to LatLng var
            LatLng currentLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            //animating map camera to the current user's location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f));

            //setting map click listener
            mMap.setOnMapClickListener(this);

            // check network state is connected or not
            if (conMgr != null && (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {

                // user is online, get data from firebase databse
                mReviewsDatabase.readAllReviews(this);

            } else if (conMgr != null && (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED)) {

                // notify user you are not online
                Toast.makeText(this, "Failed to read reviews, Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Extracting place name
     * Assigning place Latitude and Longitude
     * Moving map camera to the selected place
     * Displaying a review dialog to user
     *
     * @param place is the picked place via user
     */
    private void onPlaceSelected(Place place) {
        try {
            //declaring location var to hold latitude and longitude
            Location location = new Location("selected location");
            //assigning selected place latitude
            location.setLatitude(place.getLatLng().latitude);
            //assigning selected place longitude
            location.setLongitude(place.getLatLng().longitude);

            //assigning selected place name to use it
            // while adding review to firebase database
            pickedPlaceName = place.getName().toString();

            //assigning selected place LatLng to move map camera to it
            pickedPlaceLocation = new LatLng(location.getLatitude(), location.getLongitude());

            //Moving the map camera to selected place
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickedPlaceLocation, 16f));

            //preparing dialog with selected place name
            prepareDialog(pickedPlaceName);

            // displaying review dialog
            mReviewDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This Method Prepare the Review and Rating Dialog
     *
     * @param placeName is the selected place name
     */
    private void prepareDialog(String placeName) {
        try {
            // declaring dialog var
            mReviewDialog = new Dialog(this);

            // Assigning dialog content view
            mReviewDialog.setContentView(R.layout.custom_rating_dialog);

            // Setting dialog title with custom title
            mReviewDialog.setTitle("did you like" + placeName + " ?");

            // set the custom dialog components - text, image and button
            stars[0] = mReviewDialog.findViewById(R.id.rating_star_0);
            stars[1] = mReviewDialog.findViewById(R.id.rating_star_1);
            stars[2] = mReviewDialog.findViewById(R.id.rating_star_2);
            stars[3] = mReviewDialog.findViewById(R.id.rating_star_3);
            stars[4] = mReviewDialog.findViewById(R.id.rating_star_4);

            //Assigning rating stars ClickListener callback
            for (int i = 0; i < 5; i++)
                stars[i].setOnClickListener(this);

            //Declaring dialog review comment EditText
            reviewContentEditText = mReviewDialog.findViewById(R.id.rating_dialog_comment_edit_text);

            //Declaring dialog submit button
            Button submitButton = mReviewDialog.findViewById(R.id.rating_submit_button);

            //Declaring dialog cancel button
            Button cancelButton = mReviewDialog.findViewById(R.id.rating_cancel_button);

            // if submit button is clicked, add review to Firebase database
            submitButton.setOnClickListener(this);

            // if cancel button is clicked, close the custom dialog
            cancelButton.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates user's location
     *
     * @param location which is the new location of the user
     */
    @Override
    public void onLocationChanged(Location location) {
        try {
            Log.i("onLocationChanged", "called");
            Log.i("onLocationChanged", "Lat = " + location.getLongitude() + " Lng = " + location.getLongitude());

            //declaring LatLng with current location
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            //updating current location var
            mCurrentLocation = location;

            //Moving map camera to the new location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler Map click
     * Opens Place Pick Builder to choose a specific place
     *
     * @param latLng the selected location LatLng via the user
     */
    @Override
    public void onMapClick(LatLng latLng) {
        try {
            //declaring a place LatLng Bounds
            LatLngBounds latLngBounds = new LatLngBounds(latLng, latLng);

            //Setting Place Builder with a selected place LatLng Bounds
            builder.setLatLngBounds(latLngBounds);

            //starting Place Picker Builder
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler UI Components Click Listener
     */
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                //Handler Submit button Click Listener
                case R.id.rating_submit_button:
                    try {
                        //resting stars to default state
                        fillStars(-1);

                        //handle review comment added in EditText
                        ReviewModel review;
                        if (reviewContentEditText.getText() != null && !reviewContentEditText.getText().toString().isEmpty()) {
                            review = new ReviewModel(String.valueOf(ratingStarsCount), String.valueOf(pickedPlaceLocation.latitude),
                                    String.valueOf(pickedPlaceLocation.longitude), reviewContentEditText.getText().toString(), pickedPlaceName);
                        }
                        //handle no review comments inserted
                        else {
                            review = new ReviewModel(String.valueOf(ratingStarsCount), String.valueOf(pickedPlaceLocation.latitude),
                                    String.valueOf(pickedPlaceLocation.longitude), "", pickedPlaceName);
                        }

                        // add review to firebase
                        mReviewsDatabase.addReview(review, this);

                        //resting review comment EditText to default state
                        reviewContentEditText.setText("");

                        //Closing the review dialog
                        mReviewDialog.dismiss();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;

                //Handler Cancel button Click Listener
                case R.id.rating_cancel_button:
                    //Closing the review dialog
                    mReviewDialog.dismiss();
                    break;
                //Handler star number 1 Click Listener
                case R.id.rating_star_0:
                    //saving stars count
                    ratingStarsCount = 1;
                    //fill selected stars and resting others
                    fillStars(0);
                    break;
                //Handler star number 2 Click Listener
                case R.id.rating_star_1:
                    //saving stars count
                    ratingStarsCount = 2;
                    //fill selected stars and resting others
                    fillStars(1);
                    break;
                //Handler star number 3 Click Listener
                case R.id.rating_star_2:
                    //saving stars count
                    ratingStarsCount = 3;
                    //fill selected stars and resting others
                    fillStars(2);
                    break;
                //Handler star number 4 Click Listener
                case R.id.rating_star_3:
                    //saving stars count
                    ratingStarsCount = 4;
                    //fill selected stars and resting others
                    fillStars(3);
                    break;
                //Handler star number 5 Click Listener
                case R.id.rating_star_4:
                    //saving stars count
                    ratingStarsCount = 5;
                    //fill selected stars and resting others
                    fillStars(4);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * handle setting full and border stars action
     *
     * @param to is the selected star index
     */
    private void fillStars(int to) {

        //setting fulled stars from star 0 to specified star through number
        for (int i = 0; i <= to; i++) {
            stars[i].setImageResource(R.drawable.ic_star_black_24dp);
        }
        // resetting stars which is not counted through rating action
        if (to < 4)
            for (int i = to + 1; i <= 4; i++)
                stars[i].setImageResource(R.drawable.ic_star_border_black_24dp);
    }


    // ReviewDatabaseListener Callbacks

    /**
     * Receiving callback from Review Database after adding review
     * into firebase database successfully
     * <p>
     * Adding Marker on the place that had been reviewed via user
     *
     * @param review is the add review into Firbase Database
     */
    @Override
    public void onAddReviewSuccess(final ReviewModel review) {
        try {
            //Using a Handler and associate the Main Thread Looper to it
            //we post a runnable on MAIN UI Thread to update App UI components
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //adding submitted review to the list of submitted reviews
                        submittedReviews.add(review);

                        //declaring and assigning marker title with submitted review details
                        String markerTitle = review.getmRatingStarsCount().concat("stars\n");

                        //setting location LatLng
                        LatLng location = new LatLng(Double.parseDouble(review.getmLatitude()),
                                Double.parseDouble(review.getmLongtitude()));

                        //declaring and assigning reviewed place marker to display on the map
                        MarkerOptions markerOptions = new MarkerOptions().position(location).title(markerTitle);

                        // setting marker snippet with review content
                        markerOptions.snippet(review.getmReviewContent());

                        //adding the marker to markers arrayList
                        mMarkerOptions.add(markerOptions);

                        //adding the marker to the map
                        mMap.addMarker(markerOptions);

                        //displaying toast to user to inform with updates of adding review
                        Toast.makeText(MapsActivity.this, "Review add successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddReviewFailure(Exception e) {

        Log.i("onAddReviewFailure", e.getMessage());
    }

    @Override
    public void onReadAllReviewSuccess(final ArrayList<ReviewModel> reviews) {
        try {
            //Using a Handler and associate the Main Thread Looper to it
            //we post a runnable on MAIN UI Thread to update App UI components
            // with reviews had been read from database
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //iterate over list of reviews
                        for (ReviewModel review : reviews) {
                            try {
                                //declaring and assigning marker title with submitted review details
                                String markerTitle = review.getmRatingStarsCount().concat(" stars\n");

                                //setting location LatLng
                                LatLng location = new LatLng(Double.parseDouble(review.getmLatitude()),
                                        Double.parseDouble(review.getmLongtitude()));

                                //declaring and assigning reviewed place marker
                                MarkerOptions markerOptions = new MarkerOptions().position(location).title(markerTitle);

                                //assigning marker snippet with review content
                                markerOptions.snippet(review.getmReviewContent());
                                //adding the marker to markers arrayList
                                mMarkerOptions.add(markerOptions);

                                //adding the marker to the map
                                mMap.addMarker(markerOptions);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadAllReviewFailure(Exception e) {
        Log.i("onReadAllReviewFailure", e.getMessage());
    }
}
