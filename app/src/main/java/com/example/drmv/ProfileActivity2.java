package com.example.drmv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.backendless.persistence.DataQueryBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity2 extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button edita,edith,editw,confirm;
    private EditText age,height,weight;
    private EditText name,mob;
    private EditText pf2gen,pf2diab;
    private float getage,getht,getwt,bmi;
    private double bmr;
    private EditText bmi11,bmr11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        edita=(Button)findViewById(R.id.pf2_editage);
        edith=(Button)findViewById(R.id.pf2_editheight);
        editw=(Button)findViewById(R.id.pf2_editweight);
        confirm=(Button)findViewById(R.id.confirmbtn);
        age=(EditText)findViewById(R.id.pf2_age);
        height=(EditText)findViewById(R.id.pf2_ht);
        weight=(EditText)findViewById(R.id.pf2_wt);
        name=(EditText)findViewById(R.id.pf_name);
        mob=(EditText)findViewById(R.id.pf_mob);
        pf2gen=(EditText) findViewById(R.id.pf2_gen);
        pf2diab=(EditText) findViewById(R.id.pf2_diab);
        bmi11=(EditText)findViewById(R.id.bmi11);
        bmr11=(EditText)findViewById(R.id.bmr11);
        age.setEnabled(false);
        height.setEnabled(false);
        weight.setEnabled(false);
        name.setEnabled(false);
        pf2gen.setEnabled(false);
        pf2diab.setEnabled(false);
        mob.setEnabled(false);
        bmi11.setEnabled(false);
        bmr11.setEnabled(false);


        Backendless.setUrl("https://api.backendless.com");
        Backendless.initApp( getApplicationContext(),
                "7EE1882F-B7A1-94F7-FF91-18F2455F3400",
                "31F81F07-D198-4F73-A87F-B8E28BBDE1AC" );
        //=============================================================
        String select="Email='"+firebaseUser.getEmail()+"'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause( select );
        final ArrayList<Map> l=new ArrayList<Map>();
        Backendless.Data.of("userinfo").find(
                queryBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        l.add(response.get(0));
                       name.setText(l.get(0).get("Name").toString());
                       mob.setText(l.get(0).get("Mobile").toString());
                       weight.setText(l.get(0).get("Weight").toString());
                       height.setText(l.get(0).get("Height").toString());
                       age.setText(l.get(0).get("Age").toString());
                       bmi11.setText(l.get(0).get("BMI").toString());
                       bmr11.setText(l.get(0).get("BMR").toString());
                       pf2gen.setText(l.get(0).get("Gender").toString());
                       //pf2diab.setText("Diabetes : "+l.get(0).get("Diabetes").toString());
                       if(l.get(0).get("Diabetes").toString().equals("-ve"))
                       {
                           pf2diab.setText("Veg");
                       }
                       else
                       {
                           pf2diab.setText("Non-Veg");
                       }

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                }
        );

        //============================================================================================
        edita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age.setEnabled(true);
            }
        });

        edith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height.setEnabled(true);
            }
        });

        editw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight.setEnabled(true);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getage=Float.parseFloat(age.getText().toString());
                getht=Float.parseFloat(height.getText().toString());
                getwt=Float.parseFloat(weight.getText().toString());

                if((pf2gen.getText().toString()).equals("Male"))
                {
                    bmr=((10*getwt)+(6.25*getht)-(5*getage)+5);
                }
                else {
                    bmr=((10*getwt)+(6.25*getht)-(5*getage)-161);
                }
                bmi=(getwt/(getht*getht))*10000;
              //==========================================================
                RequestQueue requestQueue=Volley.newRequestQueue(ProfileActivity2.this);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Uid",firebaseAuth.getUid());
                params.put("Age",age.getText().toString());
                params.put("Weight",weight.getText().toString());
                params.put("Height",height.getText().toString());
                params.put("BMR",String.valueOf(bmr));
                params.put("BMI",String.valueOf(bmi));

                String url = "https://secure-tundra-65917.herokuapp.com/profile/update";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Process os success response
                               // Log.e("Response ", response.toString());
                               Toast.makeText(ProfileActivity2.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.toString());
                    }
                });

                requestQueue.add(postRequest);
         //========================================================================

        HashMap testage = new HashMap<>();
        testage.put("Age",getage);
        testage.put("Height",getht);
        testage.put("Weight",getwt);
        testage.put("BMI",bmi);
        testage.put("BMR",bmr);

        final String where="Email='"+firebaseUser.getEmail()+"'";

        Backendless.Data.of("userinfo").update(where, testage, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
               // Toast.makeText(ProfileActivity2.this,"Saved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(ProfileActivity2.this,where,Toast.LENGTH_SHORT).show();
            }
        });
        //===================================

            }
        });

    }
}
