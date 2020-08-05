package com.example.drmv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
   private TextView signup1,for_pass;
   private Button login1,regi;
   private EditText email_login;
   private EditText pass_login;
    private FirebaseAuth firebaseAuth;
 //   private ProgressDialog progess;
    private FirebaseUser userrr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup1 = (TextView) findViewById(R.id.emailhead);
        login1=(Button)findViewById(R.id.login);
        email_login=(EditText)findViewById(R.id.editText6);
        pass_login=(EditText)findViewById(R.id.editText7);
        firebaseAuth=FirebaseAuth.getInstance();
        userrr=firebaseAuth.getInstance().getCurrentUser();
        for_pass=(TextView)findViewById(R.id.textView3);
        regi=(Button)findViewById(R.id.button3);
       // progess=new ProgressDialog(this);


        if(userrr != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,HomeActivity2.class));
        }


        regi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        //login1.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
         //       if(validate1())
         //       {
         //           valid_login(email_login.getText().toString(),pass_login.getText().toString());
         //       }
         //   }
        //});

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate1())
                {
                    valid_login(email_login.getText().toString(),pass_login.getText().toString());
                }
            }
        });

        for_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });

    }

    private boolean validate1()
    {
        boolean result1=false;
        String email1=email_login.getText().toString();
        String pass1=pass_login.getText().toString();
        if(Pattern.matches("^(.+)@(.+)$",email1) && Pattern.matches("^[a-zA-Z][a-zA-Z0-9]{5,9}+$",pass1))
        {
            result1=true;
        }
        else
        {
            Toast.makeText(this,"Please Fill Complete Details !!!\n Password(alphanumeric starting with letter in range 6-10)",Toast.LENGTH_SHORT).show();

        }

        return  result1;
    }

    private void valid_login(String email, String pass)
    {
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Failed !!", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    //Toast.makeText(MainActivity.this, "Login Sucessfull !!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this,HomeActivity2.class));
                    check_verification();
                    finish();

                }
            }
        });

    }
    private void check_verification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        boolean flag_ver=firebaseUser.isEmailVerified();
        if(flag_ver)
        {
            startActivity(new Intent(this,HomeActivity2.class));
        }
        else
        {
            Toast.makeText(this,"Verify Email !!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}
