package com.pooja.canteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {

    private TextView singin;
    private AppCompatButton singup;
    private EditText name,email,password,repassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2 ;
    ImageView userImageProfileView;
    String TAG="tag";
    Uri imageHoldUri = null;

    ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=  findViewById(R.id.et_name);
        email= findViewById(R.id.et_email);
        password=  findViewById(R.id.et_password);
        repassword=  findViewById(R.id.et_reEnterPassword);
        singin= findViewById(R.id.link_login);
        singup=  findViewById(R.id.btn_create);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef= FirebaseDatabase.getInstance().getReference().child("user");
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        mProgress = new ProgressDialog(this);

    }

    private void signUp() {
        final String names=name.getText().toString().trim();
        final String emails=email.getText().toString().trim();
        String passwords=password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgress.setTitle("Saving Profile");
                            mProgress.setMessage("Please wait....");
                            mProgress.show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final String uid=mAuth.getCurrentUser().getUid();
                            mRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                            mRef.child("name").setValue(names);
                            mRef.child("email").setValue(emails);
                            mRef.child("uid").setValue(uid);
                          // mRef.child("imageurl").setValue(imageUrl.toString());
                            mProgress.dismiss();


                            Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

}
