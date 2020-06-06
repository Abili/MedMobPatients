package com.raisac.medmobpatients;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class OrderDoctor extends BottomSheetDialogFragment implements GoogleMap.OnPoiClickListener {

    private static final String TAG = "ChangePhotoDialog";

    public static final int CAMERA_REQUEST_CODE = 5467;//random number
    public static final int PICKFILE_REQUEST_CODE = 8352;//random number
    private Spinner mMSufferingFrom;
    private String mMPatientSufering;
    private String mPlace;
    private Button mOrder;
    public EditText mMMSufferingFrom;
    public EditText mLocation;

    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getContext(), "Clicked: " +
                        poi.name + "\nPlace ID:" + poi.placeId +
                        "\nLatitude:" + poi.latLng.latitude +
                        " Longitude:" + poi.latLng.longitude,
                Toast.LENGTH_SHORT).show();
        mPlace = poi.name;
    }

    public interface OnPhotoReceivedListener {
        public void getImagePath(Uri imagePath);

        public void getImageBitmap(Bitmap bitmap);
    }

    OnPhotoReceivedListener mOnPhotoReceived;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.patient_request_form, container, false);

        //Initialize the textview for choosing an image from memory
        mOrder = view.findViewById(R.id.request_for_doctor);
        mMMSufferingFrom = view.findViewById(R.id.sufferingDescribe);
        mLocation = view.findViewById(R.id.location);

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get text from the patient
                //get from the suffering_from information
                mLocation.setText(mPlace);
                String Location = mLocation.getText().toString();

                /* get information from the spinner and use it to match is to the doctors vailable online
                 * depnding on the inforation they have given*/

                String mMPatientSufering = mMMSufferingFrom.getText().toString();
                /*
                 * get the location of the patient and triangulate it to search for a close by
                 * doctor that matches the patient' information of what they are suffering from

                 */

            }
        });


        return view;
    }


}

















