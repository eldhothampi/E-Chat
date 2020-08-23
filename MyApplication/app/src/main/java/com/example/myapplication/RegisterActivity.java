package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = new String();
    EditText txtName,txtPhone,txtEmail,txtPassword;
Button btnRegister;
TextView btnLogin;
FirebaseAuth fAuth;
ProgressBar pBar;
FirebaseFirestore fStore;
String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtName=findViewById(R.id.txtName);
        txtPhone=findViewById(R.id.txtPhone);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPass);
        btnRegister=findViewById(R.id.btnRegister);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        pBar=findViewById(R.id.progressBar);
        btnLogin=findViewById(R.id.btnSignin);

       // if(fAuth.getCurrentUser()!=null){
       //     startActivity(new Intent(getApplicationContext(),MainActivity.class));
       //     finish();
       //  }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=txtEmail.getText().toString().trim();
                String pass=txtPassword.getText().toString().trim();
                final String name=txtName.getText().toString().trim();
                final String phone=txtPhone.getText().toString().trim();
                if(TextUtils.isEmpty(name))
                {
                    txtName.setError("Name Required");
                    return;
                }
                if(TextUtils.isEmpty(phone))
                {
                    txtPhone.setError("Phone Required");
                    return;
                }
                if(phone.length()<10)
                {
                    txtPhone.setError("Invalid Phone");
                }
                if(TextUtils.isEmpty(email))
                {
                    txtEmail.setError("Email Required");
                    return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    txtPassword.setError("Password Required");
                    return;
                }
                if(pass.length()<6){
                    txtPassword.setError("Password must contain 6 characters ");
                    return;
                }
                pBar.setVisibility(View.VISIBLE);

                // Registration From Here !!!!!!!!

                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("user").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("name",name);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: ");
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Login.class));
                            pBar.setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error..!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pBar.setVisibility(View.INVISIBLE);
                        }



                    }
                });

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

    }


}
