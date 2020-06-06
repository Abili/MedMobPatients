package com.raisac.medmobpatients;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPoiClickListener {

    private static final String CHANNEL_ID = "Incoming notification";
    private static final int NOTIFICATION_ID = 1;
    private GoogleMap mMap;
    private Marker pickUpMarker;
    int radius = 1;
    String doctorFoundId;
    Boolean doctorFound = false;

    private LocationRequest mLocationRequest;

    @BindView(R.id.card_doc)
    CardView mCardDoc;
    @BindView(R.id.patients_phone)
    TextView docs_phoneNumber;
    @BindView(R.id.doc_name)
    TextView mDocsName;
    @BindView(R.id.call_doc)
    Button callPatient;
    @BindView(R.id.closeInfo)
    Button close_info;
    @BindView(R.id.doc_pic)
    ImageView mDocs_image;

    private String destination;

    private GeofencingClient geofencingClient;

    private static final String TAG = MapsActivity.class.getSimpleName();
    private CameraPosition mCameraPosition;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private String mUid;
    private DatabaseReference mLocation_ref;
    private GeoFire mGeoFire;

    String users = "users";
    String location = "location";
    String patients = "patientsRequests";
    private String mPlace;
    private LatLng mMyposition;
    String mDoctors = "doctorsAvailable";
    String doctors = "doctors";
    private DatabaseReference mDriver_available;
    String doctorsWorking = "doctorsWorking";
    Button mOrderDoc;
    private NotificationCompat.Builder mBuilder;
    private GeoQuery mGeoQuery;
    private Boolean requestBool = false;
    private Marker doctorMarker;
    private DatabaseReference mDoctorLocationRef;
    private ValueEventListener mDoctorLocationEventListener;
    private DatabaseReference mPatientDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        init(user);
        mOrderDoc = findViewById(R.id.orderdocBtn);

        geofencingClient = LocationServices.getGeofencingClient(this);

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUid != null) {
            mUid = mUser.getUid();
        }

        mLocation_ref = FirebaseDatabase.getInstance().getReference().child(patients);
        mGeoFire = new GeoFire(mLocation_ref);
        close_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardDoc.setVisibility(View.GONE);
            }
        });

        callPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callDoctor();
            }
        });
    }

    public void orderDoc(View view) {
        if (requestBool) {
            cancelDocotor();

        } else {
            requestBool = true;

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(mMyposition));
            mGeoFire = new GeoFire(mLocation_ref);
            if (mLastKnownLocation != null) {
                mGeoFire.setLocation(mUid, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                mMyposition = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                pickUpMarker = mMap.addMarker(new MarkerOptions().position(mMyposition).title("Me"));

                getClosestDoctor();
            }
        }

    }


    private void cancelDocotor() {


//        OrderDoctor dialog = new OrderDoctor();
//        dialog.show(getSupportFragmentManager(), TAG);
        if (requestBool) {
            requestBool = false;
            mGeoQuery.removeAllListeners();
            mDoctorLocationRef.removeEventListener(mDoctorLocationEventListener);
            if (doctorFoundId != null) {
                mPatientDatabase.child("patientReqId").setValue(true);
                doctorFoundId = null;
            }
            doctorFound = false;
            radius = 1;
            mGeoFire.removeLocation(mUid);
            if (pickUpMarker != null) {
                pickUpMarker.remove();
                doctorMarker.remove();
            }

            mOrderDoc.setText("call doctor");
            mCardDoc.setVisibility(View.GONE);
            docs_phoneNumber.setText("");
            mDocsName.setText("");
            mDocs_image.setImageResource(R.drawable.profile_pic);


        } else {
            requestBool = true;

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(mMyposition));
            mGeoFire = new GeoFire(mLocation_ref);
            if (mLastKnownLocation != null) {
                mGeoFire.setLocation(mUid, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                mMyposition = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                pickUpMarker = mMap.addMarker(new MarkerOptions().position(mMyposition).title("Me"));

                getClosestDoctor();
            }
        }

    }

    private void init(FirebaseUser firebaseUser) {
        Intent getProfle = getIntent();
        String photoUrl = getProfle.getStringExtra("user_image");

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MapsActivity.this));
        //getUserAccountData();


    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    /**
     * Sets up the options menu.
     *
     * @param menu The options menu.
     * @return Boolean.
     */


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */

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
        mMap = googleMap;
        googleMap.setOnPoiClickListener(this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //set location to firebase
//            if (mLastKnownLocation!=null) {
//                mGeoFire.setLocation(mUid, new GeoLocation(mLastKnownLocation.getLatitude(),
//                        mLastKnownLocation.getLongitude()));
//            }

        } else {
            getLocationPermission();
        }

        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.styled_map));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Add some markers to the map, and add a data object to each marker.


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mLastKnownLocation = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));


                }
            }
        }
    };

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                            Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    mPlacesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        mLikelyPlaceNames = new String[count];
                        mLikelyPlaceAddresses = new String[count];
                        mLikelyPlaceAttributions = new List[count];
                        mLikelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            mLikelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        MapsActivity.this.openPlacesDialog();
                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getClosestDoctor() {
        mOrderDoc.setText("Getting Doctor...");
        mDriver_available = FirebaseDatabase.getInstance().getReference().child(mDoctors);
        GeoFire geoFire = new GeoFire(mDriver_available);
        mGeoQuery = geoFire.queryAtLocation(new GeoLocation(mLastKnownLocation.getLatitude(),
                mLastKnownLocation.getLongitude()), radius);
        mGeoQuery.removeAllListeners();

        mGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {


            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!doctorFound && requestBool) {
                    doctorFound = true;
                    doctorFoundId = key;
                    Toast.makeText(MapsActivity.this, "Linked to a doctor: " + mDocsName, Toast.LENGTH_SHORT).show();

                    mPatientDatabase = FirebaseDatabase.getInstance().getReference()
                            .child(users).child(doctors).child(key);
                    mPatientDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                Map<String, Object> docMap = (Map<String, Object>) dataSnapshot.getValue();
                                if (doctorFound) {
                                    return;
                                }

                                DatabaseReference mDoctorRef = FirebaseDatabase.getInstance().getReference()
                                        .child(users).child(doctors).child(doctorFoundId).child(patients);
                                String patientReqId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                HashMap<String, Object> custHashMap = new HashMap<>();
                                custHashMap.put("patientReqId", patientReqId);
                                mDoctorRef.updateChildren(custHashMap);

                                //show driver in customer map
                                getDoctorLocation();
                                getDoctorInfo();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }


            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
                if (!doctorFound) {
                    radius++;
                    getClosestDoctor();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void getDoctorInfo() {
        mCardDoc.setVisibility(View.VISIBLE);

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(doctors).child(doctorFoundId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    if (dataSnapshot.child("fName") != null) {
                        mDocsName.setText(dataSnapshot.child("fName").getValue().toString());
                    }
                    if (dataSnapshot.child("profile_pics") != null) {
                        //Glide.with(getApplicationContext()).load(map.get("profile_pics").toString()).into(mDocs_image);
                        ImageLoader.getInstance().displayImage(dataSnapshot.child("profile_pics").getValue().toString(), mDocs_image);
                    }
                    if (dataSnapshot.child("phone") != null) {
                        docs_phoneNumber.setText(dataSnapshot.child("phone").getValue().toString());
                    }

                } else {
                    mDocsName.setText("No Name");
                    docs_phoneNumber.setText("No Number");
                    // Toast.makeText(Profile.this, "Data not Found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getDoctorLocation() {
        mDoctorLocationRef = FirebaseDatabase.getInstance().getReference()
                .child(doctorsWorking).child(doctorFoundId).child("l");

        mDoctorLocationEventListener = mDoctorLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBool) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLong = 0;


                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());

                    }
                    if (map.get(1) != null) {
                        locationLong = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng doctorLatLong = new LatLng(locationLat, locationLong);
                    if (doctorMarker != null) {
                        doctorMarker.remove();
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(mMyposition.latitude);
                    loc1.setLongitude(mMyposition.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(doctorLatLong.latitude);
                    loc2.setLongitude(doctorLatLong.longitude);

                    float distance = (int) loc1.distanceTo(loc2);

                    //setText on button
                    String doctFound = "You've been inked to a doctor: " + String.valueOf(distance) + " Meters Away";
                    mOrderDoc.setText(doctFound);
                    createNotification(doctFound);

                    if (distance < 20) {
                        String doctHere = "Doctor Has Reached";
                        mOrderDoc.setText(doctHere);
                        createNotification(doctHere);
                    }


                    doctorMarker = mMap.addMarker(new MarkerOptions().position(doctorLatLong).title("Doctor")
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_doctor_foreground)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void createNotification(String info) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Intent notifyIntent = new Intent(this, MapsActivity.class);
// Set the Activity to start in a new, empty task
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.medmob_icon);

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            mBuilder.setContentText(info);
            mBuilder.setContentTitle("MedMob");
            mBuilder.setContentIntent(notifyPendingIntent);
            mBuilder.setSmallIcon(R.drawable.medmob_icon);
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            mBuilder.setContentIntent(notifyPendingIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            mBuilder.setLargeIcon(icon);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getApplicationContext(), "Clicked: " +
                        poi.name + "\nPlace ID:" + poi.placeId +
                        "\nLatitude:" + poi.latLng.latitude +
                        " Longitude:" + poi.latLng.longitude,
                Toast.LENGTH_SHORT).show();
        mPlace = poi.name;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }

}

