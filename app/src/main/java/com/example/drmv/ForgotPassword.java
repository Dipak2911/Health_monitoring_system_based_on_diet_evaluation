package com.example.drmv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private EditText email_forgot;
    private Button forgot;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        email_forgot=(EditText)findViewById(R.id.editText);
        forgot=(Button)findViewById(R.id.button5);
        firebaseAuth=FirebaseAuth.getInstance();
/*
        if(!(email_forgot.getText().toString()).isEmpty()) {
            Toast.makeText(this,"Please Enter Email !!",Toast.LENGTH_SHORT).show();
        }
        else {

            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.sendPasswordResetEmail(email_forgot.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPassword.this,"Password Reset Email Sent !!!",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                            }else {
                                Toast.makeText(ForgotPassword.this,"Error in Password Reset !!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }


 */
    forgot.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Pattern.matches("^(.+)@(.+)$",email_forgot.getText().toString()))
            {
                firebaseAuth.sendPasswordResetEmail(email_forgot.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPassword.this,"Password Reset Email Sent !!!",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                        }else {
                            Toast.makeText(ForgotPassword.this,"Error in Password Reset !!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(ForgotPassword.this,"Please Enter Valid Email !!",Toast.LENGTH_SHORT).show();

            }
        }
    });


    }
}
