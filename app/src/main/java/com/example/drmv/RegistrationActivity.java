package com.example.drmv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.drmv.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private EditText uName,uPass,uEmail;
    private Button login2;
    private TextView signin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        uName=(EditText)findViewById(R.id.editText3);
        uEmail=(EditText)findViewById(R.id.editText4);
        uPass=(EditText)findViewById(R.id.editText5);
        login2=(Button)findViewById(R.id.login2);
        signin=(TextView) findViewById(R.id.textView2);

        firebaseAuth=FirebaseAuth.getInstance();

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    // upload to database
                    String user_email=uEmail.getText().toString();
                    String user_pass=uPass.getText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_pass).addOnCompleteListener(RegistrationActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed !!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //Toast.makeText(RegistrationActivity.this, "Registration Sucessfull !!", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                               // send_mail();
                                //finish();
                               finish();
                               startActivity(new Intent(RegistrationActivity.this,ProfileActivity.class));

                            }
                        }
                    });
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }

        });

    }



    private boolean validate()
    {
        boolean result=false;
        String name=uName.getText().toString();
        String password=uPass.getText().toString();
        String email=uEmail.getText().toString();

        if(Pattern.matches("^(.+)@(.+)$",email) && Pattern.matches("^[a-zA-Z][a-zA-Z0-9]{5,9}+$",password) && Pattern.matches("[a-zA-Z ]+",name))
            {
                result=true;
            }else
            {
                Toast.makeText(this,"Please Fill Complete Details !!!\n Password(alphanumeric starting with letter in range 6-10)",Toast.LENGTH_SHORT).show();
            }
            return result;
    }
/*
    private void send_mail()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegistrationActivity.this,"Veification Mail Sent !! Verify it.",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this,"Verificstion Mail Not Sent !!",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

 */
}
