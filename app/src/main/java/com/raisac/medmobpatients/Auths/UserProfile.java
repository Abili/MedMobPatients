package com.raisac.medmobpatients.Auths;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.raisac.medmobpatients.Dialogs.ChangePhotoDialog;
import com.raisac.medmobpatients.FirebaseUtils.FilePaths;
import com.raisac.medmobpatients.MainActivity;
import com.raisac.medmobpatients.R;
import com.raisac.medmobpatients.Models.Users;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class UserProfile extends AppCompatActivity implements
        ChangePhotoDialog.OnPhotoReceivedListener {

    private static final String TAG = "UserProfile";
    ImageLoader imageLoader;
    private EditText fName;
    private EditText lName;
    private FirebaseUser mCurrentUser;
    private String mUid;
    private EditText nDateofBirth;
    private String mFirstName;
    private String mLastName;
    private String mAge1;
    private String mDeparment;
    private String mExprience;
    private String mDate_of_birth;
    private DatabaseReference mReference;
    private BottomSheetDialog mDialog;
    private ImageView mSelectPhoto;

    @Override
    public void getImagePath(Uri imagePath) {
        if (!imagePath.toString().equals("")) {
            mSelectedImageBitmap = null;
            mSelectedImageUri = imagePath;
            Log.d(TAG, "getImagePath: got the image uri: " + mSelectedImageUri);
            ImageLoader.getInstance().displayImage(imagePath.toString(), mProfileImage);
        }

    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            mSelectedImageUri = null;
            mSelectedImageBitmap = bitmap;
            Log.d(TAG, "getImageBitmap: got the image bitmap: " + mSelectedImageBitmap);
            mProfileImage.setImageBitmap(bitmap);
        }
    }

    private Button mSignUpDoctor;
    private ProgressBar mProgressBar;
    private String[] mDep;
    private ArrayAdapter<String> mMedmodDepartments;
    private EditText mEmail;
    private String[] mExp;
    private String[] age;
    private String[] mAge;
    private EditText nAge;
    private EditText mEdDoB;


    private static final int REQUEST_CODE = 1234;
    private static final double MB_THRESHHOLD = 5.0;
    private static final double MB = 1000000.0;

    Dialog imageUploadDialog;
    public static final int CAMERA_REQUEST_CODE = 5467;
    public static final int PICKFILE_REQUEST_CODE = 8352;


    //firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    private ImageView mProfileImage;
    private Button mSave;

    //private ProgressBar mProgressBar;
    private TextView mResetPasswordLink;

    //vars
    private boolean mStoragePermissions;
    private Uri mSelectedImageUri;
    private Bitmap mSelectedImageBitmap;
    private byte[] mBytes;
    private double progress;
    FirebaseAuth mAuth;
    String patients = "patients";
    //boolean isButtonEnabled = !Objects.requireNonNull(mSave).isEnabled();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patients);
        Log.d(TAG, "onCreate: started.");


        //FirebaseUser firebaseUser = mAuth.getCurrentUser();
        mProgressBar = new ProgressBar(this);


        fName = findViewById(R.id.doctor_Fname);
        lName = findViewById(R.id.doctor_Sname);
        mProfileImage = findViewById(R.id.doctor_pic);

        //verifyStoragePermissions();
        //setupFirebaseAuth();
        //init(firebaseUser);
        hideSoftKeyboard();


        mSave = findViewById(R.id.doctor_registerBtn);
        mProgressBar = findViewById(R.id.progressBar);
        mEmail = findViewById(R.id.email);
        nDateofBirth = findViewById(R.id.dateOfBirth);
        nAge = findViewById(R.id.age);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUid = mCurrentUser.getUid();

        mReference = FirebaseDatabase.getInstance().getReference();


    }


    private void init(FirebaseUser firebaseUser) {
        Intent getProfle = getIntent();
        String photoUrl = getProfle.getStringExtra("user_image");

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(UserProfile.this));
        //getUserAccountData();

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(mStoragePermissions){
//                    getPhotos();
//                }else{
//                    verifyStoragePermissions();
//                }
                getPhotos();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mCurrentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    public void signUpDoc(View view) {
        if (!fName.getText().toString().equals("")) {
            mReference.child("users")
                    .child(patients)
                    .child(mUid)
                    //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("fName")
                    .setValue(fName.getText().toString());
        }
        if (!lName.getText().toString().equals("")) {
            mReference.child("users")
                    .child(patients)
                    .child(mUid)
                    //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("lName")
                    .setValue(lName.getText().toString());
        }
        if (!nAge.getText().toString().equals("")) {
            mReference.child("users")
                    .child(patients)
                    .child(mUid)
                    //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("age")
                    .setValue(nAge.getText().toString());
        }
        if (!nDateofBirth.getText().toString().equals("")) {
            mReference.child("users")
                    .child(patients)
                    .child(mUid)
                    //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("mDateOfBirth")
                    .setValue(nDateofBirth.getText().toString());
        }

        if (!mEmail.getText().toString().equals("")) {
            mReference.child("users")
                    .child(patients)
                    .child(mUid)
                    //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("mEmail")
                    .setValue(mEmail.getText().toString());
        }



        /*------ Upload the New Photo -----*/
        if (mSelectedImageUri != null) {
            uploadNewPhoto(mSelectedImageUri);
        } else if (mSelectedImageBitmap != null) {
            uploadNewPhoto(mSelectedImageBitmap);
        }

    }


    private void getPhotos() {

        ChangePhotoDialog dialog = new ChangePhotoDialog();
        dialog.show(getSupportFragmentManager(), TAG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO rsults after selecting image from phone memory
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: image: " + selectedImageUri);

            //TODO send the bitmap and frrgment to the interface
            mProfileImage.setImageURI(selectedImageUri);
            imageUploadDialog.dismiss();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: done taking a photo.");

            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");

            mProfileImage.setImageBitmap(bitmap);
            imageUploadDialog.dismiss();

        }
    }


    /**
     * Uploads a new profile photo to Firebase Storage using a @param ***imageUri***
     *
     * @param imageUri
     */
    public void uploadNewPhoto(Uri imageUri) {
        /*
            upload a new profile photo to firebase storage
         */
        Log.d(TAG, "uploadNewPhoto: uploading new profile photo to firebase storage.");

        //Only accept image sizes that are compressed to under 5MB. If thats not possible
        //then do not allow image to be uploaded
        BackgroundImageResize resize = new BackgroundImageResize(null);
        resize.execute(imageUri);
    }

    /**
     * Uploads a new profile photo to Firebase Storage using a @param ***imageBitmap***
     *
     * @param imageBitmap
     */
    public void uploadNewPhoto(Bitmap imageBitmap) {
        /*
            upload a new profile photo to firebase storage
         */
        Log.d(TAG, "uploadNewPhoto: uploading new profile photo to firebase storage.");

        //Only accept image sizes that are compressed to under 5MB. If thats not possible
        //then do not allow image to be uploaded
        BackgroundImageResize resize = new BackgroundImageResize(imageBitmap);
        Uri uri = null;
        resize.execute(uri);
    }


    /**
     * 1) doinBackground takes an imageUri and returns the byte array after compression
     * 2) onPostExecute will print the % compression to the log once finished
     */
    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bm) {
            if (bm != null) {
                mBitmap = bm;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
            Toast.makeText(UserProfile.this, "compressing image", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d(TAG, "doInBackground: started.");

            if (mBitmap == null) {

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(UserProfile.this.getContentResolver(), params[0]);
                    Log.d(TAG, "doInBackground: bitmap size: megabytes: " + mBitmap.getByteCount() / MB + " MB");
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IOException: ", e.getCause());
                }
            }

            byte[] bytes = null;
            for (int i = 1; i < 11; i++) {
                if (i == 10) {
                    Toast.makeText(UserProfile.this, "That image is too large.", Toast.LENGTH_SHORT).show();
                    break;
                }
                bytes = getBytesFromBitmap(mBitmap, 100 / i);
                Log.d(TAG, "doInBackground: megabytes: (" + (11 - i) + "0%) " + bytes.length / MB + " MB");
                if (bytes.length / MB < MB_THRESHHOLD) {
                    return bytes;
                }
            }
            return bytes;
        }


        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            hideDialog();
            mBytes = bytes;
            //execute the upload
            executeUploadTask();
        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    private void executeUploadTask() {
        showDialog();
        FilePaths filePaths = new FilePaths();
//specify where the photo will be stored
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        + "/profile_image"); //just replace the old image with the new one

        if (mBytes.length / MB < MB_THRESHHOLD) {

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .setContentLanguage("en") //see nodes below
                    /*
                    Make sure to use proper language code ("English" will cause a crash)
                    I actually submitted this as a bug to the Firebase github page so it might be
                    fixed by the time you watch this video. You can check it out at https://github.com/firebase/quickstart-unity/issues/116
                     */
                    .setCustomMetadata("Mitch's special meta data", "JK nothing special here")
                    .setCustomMetadata("location", "Iceland")
                    .build();
            //if the image size is valid then we can submit to database
            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(mBytes, metadata);
            //uploadTask = storageReference.putBytes(mBytes); //without metadata

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    //Now insert the download url into the firebase database
                    //Task<Uri> firebaseURL = taskSnapshot.getStorage().getDownloadUrl();
                    Toast.makeText(UserProfile.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: firebase dowgetDownloadUrlnload url : " + uri.toString());
                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(patients)
                            .child(mUid)
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("profile_pics")
                            .setValue(uri.toString());

                    UserProfile.this.hideDialog();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(UserProfile.this, "could not upload photo", Toast.LENGTH_SHORT).show();

                    UserProfile.this.hideDialog();

                }
            });
        } else {
            Toast.makeText(this, "Image is too Large", Toast.LENGTH_SHORT).show();
        }

    }

    private void getUserAccountData() {
        Log.d(TAG, "getUserAccountData: getting the user's account information");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //this loop will return a single result
                Users users = dataSnapshot.getValue(Users.class);
//                Log.d(TAG, "onDataChange: (QUERY METHOD 1) found user: "
//                        + users.toString());

                fName.setText(users.getfName());
                lName.setText(users.getlName());
                nAge.setText(users.getAge());
                // Glide.with(UserProfile.this).load(users.getphotoUrl()).into(mProfileImage);
                ImageLoader.getInstance().displayImage(users.getProfile_pics(), mProfileImage);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Generalized method for asking permission. Can pass any array of permissions
     */
    public void verifyStoragePermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {
            mStoragePermissions = true;
        } else {
            ActivityCompat.requestPermissions(
                    UserProfile.this,
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: " + permissions[0]);

                }
                break;
        }
    }


    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Return true if the @param is null
     *
     * @param string
     * @return
     */
    private boolean isEmpty(String string) {
        return string.equals("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checkAuthenticationState();
    }



    public void dateOfBirth(View view) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DAY_OF_MONTH);
        int year = calendar.get(calendar.YEAR);

        //create date picker dialog;
        DatePickerDialog pickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nDateofBirth = findViewById(R.id.dateOfBirth);
                        nDateofBirth.setText(String.format("%d/%d/%d", dayOfMonth, month, year));

                    }
                }, year, month, date);
        pickerDialog.show();
    }


}



