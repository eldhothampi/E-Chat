package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText txtEmail,txtPassword;
    Button btnLogin;
    TextView btnReg;
    FirebaseAuth fAuth;
    ProgressBar pBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPass);
        btnReg =findViewById(R.id.btnSignup);
        fAuth=FirebaseAuth.getInstance();
        pBar=findViewById(R.id.progressBar);
        btnLogin=findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtEmail.getText().toString().trim();
                String pass=txtPassword.getText().toString().trim();
                
                if(TextUtils.isEmpty(email))
                {
                    txtEmail.setError("Enter Email");
                    return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    txtPassword.setError("Enter Password");
                    return;
                }


                // Login From Here !!!!!!!!
                pBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Connected to Google Server...Login Successfully", Toast.LENGTH_SHORT).show();
                          //  startActivity(new Intent(getApplicationContext(),Profile.class));
                            startActivity(new Intent(getApplicationContext(),HomeActvity.class));
                         //   pBar.setVisibility(View.INVISIBLE);
                        }else {
                            Toast.makeText(Login.this, "Oops ! Invalid Credentials." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pBar.setVisibility(View.INVISIBLE);
                        }


                    }
                });

            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();
            }
        });

    }
}
