package com.raisac.medmobpatients;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Profile extends AppCompatActivity implements ChangePhotoDialog.OnPhotoReceivedListener {

    private static final String TAG = "Profile";
    @BindView(R.id.patients_name)
    RelativeLayout docsName;

    @BindView(R.id.patients_phone)
    RelativeLayout docsPhone;

    @BindView(R.id.patients_email)
    RelativeLayout docsEmail;

    @BindView(R.id.nameTvPatient)
    TextView name;

    @BindView(R.id.phoneTvPatient)
    TextView phone;

    @BindView(R.id.emailTvPatient)
    TextView email;

    private BottomSheetDialog mEditProfile;
    private View mV;
    String mUid;
    ImageView docsPic;
    private Uri mSelectedImageUri;
    private Bitmap mSelectedImageBitmap;
    private byte[] mBytes;

    private static final int REQUEST_CODE = 1234;
    private static final double MB_THRESHHOLD = 5.0;
    private static final double MB = 1000000.0;

    Dialog imageUploadDialog;
    public static final int CAMERA_REQUEST_CODE = 5467;
    public static final int PICKFILE_REQUEST_CODE = 8352;
    private EditText mEdit;
    RelativeLayout mRelativeLayout;
    private TextView mConfirm;
    private TextView mCancel;
    private TextView mText_title;

    @Override
    public void getImagePath(Uri imagePath) {
        if (!imagePath.toString().equals("")) {
            mSelectedImageBitmap = null;
            mSelectedImageUri = imagePath;
            Log.d(TAG, "getImagePath: got the image uri: " + mSelectedImageUri);
            ImageLoader.getInstance().displayImage(imagePath.toString(), docsPic);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        editProfile();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        init(firebaseUser);
    }

    private void init(FirebaseUser firebaseUser) {
        Intent getProfle = getIntent();
        String photoUrl = getProfle.getStringExtra("user_image");

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Profile.this));
        //getUserAccountData();


        docsPic.setOnClickListener(view -> {
//                if(mStoragePermissions){
//                    getPhotos();
//                }else{
//                    verifyStoragePermissions();
//                }
            loadDocPic();
        });

    }

    private void editProfile() {
        mV = LayoutInflater.from(this).inflate(R.layout.edit_profile, null);
        mEditProfile = new BottomSheetDialog(this);
        mEditProfile.setContentView(R.layout.edit_profile);
        docsPic = findViewById(R.id.patients_ProfilePic);
        mEdit = mEditProfile.findViewById(R.id.profileEdtPatient);
        mConfirm = mEditProfile.findViewById(R.id.savePatient);
        mCancel = mEditProfile.findViewById(R.id.cancelPatient);
        mText_title = mEditProfile.findViewById(R.id.textTitle);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditProfile.cancel();
            }
        });


    }

    public void edit_profile(View view) {
        switch (view.getId()) {
            case R.id.patients_name:
                editProfile();
                mEdit.setHint(name.getText().toString());
                mText_title.setText("Enter your name");
                mConfirm.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(mEdit.getText().toString()) && mEdit.getText().toString().length() >= 5) {
                        DatabaseReference userProfile = FirebaseDatabase.getInstance().getReference().child("users")
                                .child("patients").child(mUid).child("fName");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("fName", mEdit.getText().toString());
                        userProfile.updateChildren(map);
                        name.setText(mEdit.getText().toString());
                        mEditProfile.cancel();
                    } else {
                        mEdit.setError("Field Required");
                    }
                });

                mEditProfile.show();

                break;
            case R.id.patients_phone:
                editProfile();
                mEdit.setHint(phone.getText().toString());
                mText_title.setText("Enter your phone number");
                mConfirm.setOnClickListener(v -> {
                    if (!mEdit.getText().toString().equals("") && mEdit.getText().toString().length() >= 5) {
                        DatabaseReference userProfile = FirebaseDatabase.getInstance().getReference()
                                .child("users").child("patients").child(mUid).child("phone");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("phone", mEdit.getText().toString());
                        userProfile.updateChildren(map);
                        phone.setText(mEdit.getText().toString());
                        mEditProfile.cancel();
                    } else {
                        mEdit.setError("field   Required");
                    }

                });
                mEditProfile.show();

                break;

            case R.id.patients_email:
                editProfile();
                mEdit.setHint(email.getText().toString());
                mText_title.setText("Enter your email address");
                mConfirm.setOnClickListener(v -> {
                    if (!mEdit.getText().toString().equals("") && mEdit.getText().toString().length() >= 5) {
                        DatabaseReference userProfile = FirebaseDatabase.getInstance().getReference()
                                .child("users").child("patients").child(mUid);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("mEmail", mEdit.getText().toString());
                        userProfile.updateChildren(map);
                        email.setText(mEdit.getText().toString());
                        mEditProfile.cancel();
                    } else {
                        mEdit.setError("field Required");

                    }


                });

                mEditProfile.show();
                break;

            case R.id.patients_ProfilePic:
                if (mSelectedImageUri != null) {
                    uploadNewPhoto(mSelectedImageUri);
                } else if (mSelectedImageBitmap != null) {
                    uploadNewPhoto(mSelectedImageBitmap);
                }
                loadDocPic();
                break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        getUserinfo();
    }

    private void getUserinfo() {
        String docId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child("patients").child(docId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Users users = dataSnapshot.getValue(Users.class);
                    name.setText(users.getfName());
                    if (map.get("phone") != null) {
                        phone.setText(map.get("phone").toString());
                    }
                    if (map.get("mEmail") != null) {
                        email.setText(map.get("mEmail").toString());
                    }
                    if (map.get("profile_pics") != null) {
                        ImageLoader.getInstance().displayImage(users.getProfile_pics(), docsPic);
                    }

                } else {
                    Toast.makeText(Profile.this, "Data not Found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            mSelectedImageUri = null;
            mSelectedImageBitmap = bitmap;
            Log.d(TAG, "getImageBitmap: got the image bitmap: " + mSelectedImageBitmap);
            docsPic.setImageBitmap(bitmap);
        }
    }

    private void loadDocPic() {

        ChangePhotoDialog dialog = new ChangePhotoDialog();
        dialog.show(getSupportFragmentManager(), TAG);
    }

    private void uploadNewPhoto(Uri selectedImageUri) {
        /*
            upload a new profile photo to firebase storage
         */
        Log.d(TAG, "uploadNewPhoto: uploading new profile photo to firebase storage.");

        //Only accept image sizes that are compressed to under 5MB. If thats not possible
        //then do not allow image to be uploaded
        BackgroundImageResize resize = new BackgroundImageResize(null);
        resize.execute(selectedImageUri);
    }

    private void uploadNewPhoto(Bitmap mSelectedImageBitmap) {
         /*
            upload a new profile photo to firebase storage
         */
        Log.d(TAG, "uploadNewPhoto: uploading new profile photo to firebase storage.");

        //Only accept image sizes that are compressed to under 5MB. If thats not possible
        //then do not allow image to be uploaded
        BackgroundImageResize resize = new BackgroundImageResize(mSelectedImageBitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO rsults after selecting image from phone memory
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: image: " + selectedImageUri);

            //TODO send the bitmap and frrgment to the interface
            docsPic.setImageURI(selectedImageUri);
            imageUploadDialog.dismiss();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: done taking a photo.");

            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");

            docsPic.setImageBitmap(bitmap);
            imageUploadDialog.dismiss();

        }
    }

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
            //showDialog();
            Toast.makeText(Profile.this, "Uploading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d(TAG, "doInBackground: started.");

            if (mBitmap == null) {

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(), params[0]);
                    Log.d(TAG, "doInBackground: bitmap size: megabytes: " + mBitmap.getByteCount() / MB + " MB");
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IOException: ", e.getCause());
                }
            }

            byte[] bytes = null;
            for (int i = 1; i < 11; i++) {
                if (i == 10) {
                    Toast.makeText(Profile.this, "That image is too large.", Toast.LENGTH_SHORT).show();
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
            //hideDialog();
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //showDialog();
        FilePaths filePaths = new FilePaths();
        //specify where the photo will be stored
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + userId
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
            UploadTask uploadTask = storageReference.putBytes(mBytes, metadata);
            //uploadTask = storageReference.putBytes(mBytes); //without metadata

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    //Now insert the download url into the firebase database
                    //Task<Uri> firebaseURL = taskSnapshot.getStorage().getDownloadUrl();
                    Toast.makeText(Profile.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: firebase dowgetDownloadUrlnload url : " + uri.toString());
                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child("patients")
                            .child(mUid)
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("profile_pics")
                            .setValue(uri.toString());

                    //Profile.this.hideDialog();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(Profile.this, "could not upload photo", Toast.LENGTH_SHORT).show();

                    //Profile.this.hideDialog();

                }
            });
        } else {
            Toast.makeText(this, "Image is too Large", Toast.LENGTH_SHORT).show();
        }
    }

}
