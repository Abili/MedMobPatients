package com.raisac.medmobpatients;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raisac.medmobpatients.Auths.UserProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRegisterButton;
    private EditText mPhoneNumber;
    boolean isEnabled = false;
    private String mButtonString;
    private EditText mVerifcatioCode;
    private Button mVerify;
    private String mPhone;
    FirebaseDatabase mDatabase;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    DatabaseReference mReference;


    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText mVerificationCode;

    //firebase auth object
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    //Getting the code sent by SMS
                    String code = phoneAuthCredential.getSmsCode();

                    //sometime the code is not detected automatically
                    //in this case the code will be null
                    //so user has to manually enter the code
                    if (code != null) {
                        mVerificationCode.setText(code);
                        //verifying the code
                        verifyVerificationCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    //storing the verification id that is sent to the user
                    mVerificationId = s;
                    mResendToken = forceResendingToken;
                }
            };
    private String mMobile;
    private TextView mResend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mRegisterButton = findViewById(R.id.patientSignUp);
        mVerify = findViewById(R.id.patientVerify);
        mResend = findViewById(R.id.buttonResend);
        mResend.setEnabled(false);
        mResend.setTextColor(Color.GRAY);

        //mVerifcatioCode = findViewById(R.id.verificationCode);
        mButtonString = mRegisterButton.getText().toString();

        mRegisterButton.setOnClickListener(this);
        mVerify.setOnClickListener(this);
        mResend.setOnClickListener(this);


        mMobile = mPhoneNumber.getText().toString().trim();


        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        mVerificationCode = findViewById(R.id.verificationCode);


        //getting mobile number from the previous activity
        //and sending the verification code to the number
//        Intent intent = getIntent();
//        String mobile = intent.getStringExtra("mobile");
//        sendVerificationCode(mMobile);
    }


    //if the automatic sms detection did not work, user can also enter the code manually
    //so adding a click listener to the button

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.patientSignUp) {
            if (mPhoneNumber.getText().toString().startsWith("+256") &&
                    mPhoneNumber.getText().toString().length() == 13) {
                mPhoneNumber.setEnabled(isEnabled);
                mVerify.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.INVISIBLE);
                sendVerificationCode(mPhoneNumber.getText().toString().trim());
                mResend.setEnabled(true);
                mResend.setTextColor(Color.BLUE);
            } else {
                mPhoneNumber.setError("Enter Valid phone Number");
            }
        } else if (id == R.id.patientVerify) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                //verification successful we will start the profile activity
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent);
            }

            String code = mVerificationCode.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                mVerificationCode.setError("Enter valid code");
                mVerificationCode.requestFocus();
                return;
            }

            //verifying the code entered manually
            verifyVerificationCode(code);
        } else if (id == R.id.buttonResend)
            resendVerificationCode(mPhoneNumber.getText().toString(), mResendToken);

    }


    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = task.getResult().getUser();

                            if (user != null) {
                                String uid = mAuth.getUid();
                                final DatabaseReference mUserDB = FirebaseDatabase.getInstance()
                                        .getReference().child("users").child("patients").child(uid);
                                mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put("phone", user.getPhoneNumber());
                                            mUserDB.setValue(userMap);

                                        }
                                        userIsLoggedIn();
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            /*Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();*/
                        }
                    }

                    private void userIsLoggedIn() {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(MainActivity.this, UserProfile.class);
                            startActivity(intent);

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            startActivity(new Intent(this, LookForDoctor.class));
        }

    }
}

