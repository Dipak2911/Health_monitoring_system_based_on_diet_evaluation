package com.example.drmv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private Button confirm;
    private EditText height,weight,mob,name,age;
    private TextView bmi,bmr;
    private RadioGroup gender,db;
    private RadioButton male,female,dbp,dbn;
    private String gen="",diab;
    private Float bmi1;
    private Double bmr1;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        confirm=(Button)findViewById(R.id.confirmbtn);
        name=(EditText)findViewById(R.id.pf_name) ;
        mob=(EditText)findViewById(R.id.pf_mob);
        height=(EditText)findViewById(R.id.pf2_ht);
        weight=(EditText)findViewById(R.id.pf2_wt);
        bmi=(TextView)findViewById(R.id.pf_bmi);
        gender=(RadioGroup)findViewById(R.id.pf_gender);
        bmr=(TextView)findViewById(R.id.pf_bmr);
        male=(RadioButton)findViewById(R.id.pf_male);
        female=(RadioButton)findViewById(R.id.pf_female);
        db=(RadioGroup)findViewById(R.id.pf_db);
        age=(EditText)findViewById(R.id.pf2_age);
        dbp=(RadioButton)findViewById(R.id.pf_dbp);
        dbn=(RadioButton)findViewById(R.id.pf_dbn);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.pf_male: {
                        gen = "Male";
                        Float a,f1,f2;
                        if(age.getText().length()==0)
                        {
                            a=0.0f;
                        }
                        else
                        {
                            a=Float.parseFloat(age.getText().toString());
                        }
                        if(height.getText().length()==0)
                        {
                            f1=0.0f;
                        }
                        else
                        {
                            f1=Float.parseFloat(height.getText().toString());
                        }

                        if(weight.getText().length()==0)
                        {
                            f2=0.0f;
                        }
                        else
                        {
                            f2=Float.parseFloat(weight.getText().toString());
                        }

                        if(gen.equals("Male"))
                        {
                            Double r=((10*f2)+(6.25*f1)-(5*a)+5);
                            bmr.setText("BMR is : "+r);
                            bmr1=r;
                        }
                        else if(gen.equals("Female"))
                        {
                            Double r=((10*f2)+(6.25*f1)-(5*a)-161);
                            bmr.setText("BMR is : "+r);
                            bmr1=r;
                        }
                        break;
                    }
                    case R.id.pf_female:{
                        gen="Female";
                        Float a,f1,f2;
                        if(age.getText().length()==0)
                        {
                            a=0.0f;
                        }
                        else
                        {
                            a=Float.parseFloat(age.getText().toString());
                        }
                        if(height.getText().length()==0)
                        {
                            f1=0.0f;
                        }
                        else
                        {
                            f1=Float.parseFloat(height.getText().toString());
                        }

                        if(weight.getText().length()==0)
                        {
                            f2=0.0f;
                        }
                        else
                        {
                            f2=Float.parseFloat(weight.getText().toString());
                        }

                        if(gen.equals("Male"))
                        {
                            Double r=((10*f2)+(6.25*f1)-(5*a)+5);
                            bmr.setText("BMR is : "+r);
                            bmr1=r;
                        }
                        else if(gen.equals("Female"))
                        {
                            Double r=((10*f2)+(6.25*f1)-(5*a)-161);

                            bmr.setText("BMR is : "+r);
                            bmr1=r;
                        }
                        break;

                    }
                }
            }
        });
        db.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.pf_dbn: {
                        diab = "-ve";
                        break;
                    }
                    case R.id.pf_dbp:{
                        diab = "+ve";
                        break;

                    }
                }
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
       //database =FirebaseDatabase.getInstance();
        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Float a,f1,f2;
                if(age.getText().length()==0)
                {
                    a=0.0f;
                }
                else
                {
                    a=Float.parseFloat(age.getText().toString());
                }
                if(height.getText().length()==0)
                {
                    f1=0.0f;
                }
                else
                {
                    f1=Float.parseFloat(height.getText().toString());
                }

                if(weight.getText().length()==0)
                {
                    f2=0.0f;
                }
                else
                {
                    f2=Float.parseFloat(weight.getText().toString());
                }

                if(gen.equals("Male"))
                {
                    Double r=((10*f2)+(6.25*f1)-(5*a)+5);
                    bmr.setText("BMR is : "+r);
                    bmr1=r;
                }
                else if(gen.equals("Female"))
                {
                    Double r=((10*f2)+(6.25*f1)-(5*a)-161);
                    bmr.setText("BMR is : "+r);
                    bmr1=r;
                }
                else {
                    bmr.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
           Float f2,f1,a;
            if(height.getText().length()==0)
            {
                f1=0.0f;
            }
            else
            {
                f1=Float.parseFloat(height.getText().toString());
            }

            if(weight.getText().length()==0)
            {
                f2=0.0f;
            }
            else
            {
                f2=Float.parseFloat(weight.getText().toString());
            }
            if(age.getText().length()==0)
            {
                a=0.0f;
            }
            else {
                a=Float.parseFloat(age.getText().toString());
            }
            // bmi.setText((Integer.parseInt(height.getText().toString()) + Integer.parseInt(weight.getText().toString())));
            Float i=(f2/(f1*f1))*10000;

            if(gen.equals("Male"))
            {
                Double r=((10*f2)+(6.25*f1)-(5*a)+5);
                bmr.setText("BMR is : "+r);
                bmr1=r;
            }
            else if(gen.equals("Female"))
            {
                Double r=((10*f2)+(6.25*f1)-(5*a)-161);
                bmr.setText("BMR is : "+r);
                bmr1=r;
            }
            else {
                bmr.setText("");
            }
            bmi.setText("BMI is : "+i);
            bmi1=i;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Float f2,f1,a;
                if(height.getText().length()==0)
                {
                    f1=0.0f;
                }
                else
                {
                    f1=Float.parseFloat(height.getText().toString());
                }

                if(weight.getText().length()==0)
                {
                    f2=0.0f;
                }
                else
                {
                    f2=Float.parseFloat(weight.getText().toString());
                }
                if(age.getText().length()==0)
                {
                    a=0.0f;
                }
                else {
                    a=Float.parseFloat(age.getText().toString());
                }
                Float i=((f2/(f1*f1))*10000);

                if(gen.equals("Male"))
                {
                    Double r=((10*f2)+(6.25*f1)-(5*a)+5);
                    bmr.setText("BMR is : "+r);
                    bmr1=r;
                }
                else if(gen.equals("Female"))
                {
                    Double r=((10*f2)+(6.25*f1)-(5*a)-161);
                    bmr.setText("BMR is : "+r);
                    bmr1=r;
                }
                else{
                    bmr.setText("");
                }
                bmi.setText("BMI is : "+i);
                bmi1=i;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


confirm.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

if(validate())
{
    Backendless.setUrl("https://api.backendless.com");
    Backendless.initApp( getApplicationContext(),
            "7EE1882F-B7A1-94F7-FF91-18F2455F3400",
            "31F81F07-D198-4F73-A87F-B8E28BBDE1AC" );
    HashMap test=new HashMap<>();
    test.put("Uid",firebaseAuth.getUid());
    test.put("Email",firebaseUser.getEmail());
    test.put("Name",name.getText().toString());
    test.put("Mobile",mob.getText().toString());
    test.put("Height",Float.parseFloat(height.getText().toString()));
    test.put("Weight",Float.parseFloat(weight.getText().toString()));
    test.put("BMI",bmi1);
    test.put("Age",Float.parseFloat(age.getText().toString()));
    test.put("BMR",bmr1);
    test.put("Gender",gen);
    test.put("Diabetes",diab);

    Backendless.Data.of("userinfo").save(test, new AsyncCallback<Map>() {
        @Override
        public void handleResponse(Map response) {

        }

        @Override
        public void handleFault(BackendlessFault fault) {
            Toast.makeText(ProfileActivity.this,"not-saved",Toast.LENGTH_SHORT).show();
        }
    });


    RequestQueue requestQueue=Volley.newRequestQueue(ProfileActivity.this);
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("Uid",firebaseAuth.getUid());
    params.put("Name",name.getText().toString());
    params.put("Mobile",mob.getText().toString());
    params.put("Height",height.getText().toString());
    params.put("Weight",weight.getText().toString());
    params.put("BMI",bmi1.toString());
    params.put("Age",age.getText().toString());
    params.put("BMR",bmr1.toString());
    params.put("Gender",gen);
    params.put("Diabetes",diab);

    String url = "https://secure-tundra-65917.herokuapp.com/profile";

    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Process os success response
                    Log.e("Response ", response.toString());
                    //Toast.makeText(ProfileActivity.this, "Received"+response.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this, "Successful !", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Error: ", error.toString());
        }
    });

    requestQueue.add(postRequest);
    send_mail();
    finish();
}

    }
});

    }

    private boolean validate()
    {
        boolean result1=false;
        String name1,mob1,height1,weight1,age1;
        name1=name.getText().toString();
        mob1=mob.getText().toString();
        height1=height.getText().toString();
        weight1=weight.getText().toString();
        age1=age.getText().toString();
        if(name1.isEmpty() || mob1.isEmpty() || height1.isEmpty() || weight1.isEmpty() || age1.isEmpty() || !(male.isChecked() || female.isChecked()) || !(dbp.isChecked() || dbn.isChecked()))
        {
            Toast.makeText(this,"Please Fill Complete Details !!!",Toast.LENGTH_SHORT).show();
        }
        else {
            result1=true;
        }
        return result1;
    }

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
                        Toast.makeText(ProfileActivity.this,"Veification Mail Sent !! Verify it.",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                    }
                    else {
                        Toast.makeText(ProfileActivity.this,"Verificstion Mail Not Sent !!",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}
