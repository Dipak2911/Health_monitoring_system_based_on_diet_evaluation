package com.example.drmv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;


public class insertFood extends AppCompatActivity {

    private Spinner foodtype;
    private EditText fooditem,fat,protein,carb,calorie;
    private RadioButton veg,nonveg;
    private RadioGroup vn;
    private String foodtype1,vn1="Veg";
    private Button adddish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_food);

        fooditem=(EditText)findViewById(R.id.fooditem);
        fat=(EditText)findViewById(R.id.fat);
        protein=(EditText)findViewById(R.id.protein);
        carb=(EditText)findViewById(R.id.carb);
        calorie=(EditText)findViewById(R.id.calorie);
        vn=(RadioGroup)findViewById(R.id.vn);
        foodtype=(Spinner)findViewById(R.id.foodtype);
        adddish=(Button)findViewById(R.id.adddish);

        String[] items = new String[]{"Main Course", "BreakFast", "Snack"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        foodtype.setAdapter(adapter);


        foodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodtype1=foodtype.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                foodtype1="Main Course";
            }
        });

        vn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.veg: {
                        vn1 = "Veg";
                        break;
                    }
                    case R.id.nonveg: {
                        vn1="Non-Veg";
                        break;
                    }
                }
            }
        });

        adddish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        if(validate())
        {
            RequestQueue requestQueue= Volley.newRequestQueue(insertFood.this);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Food",fooditem.getText().toString());
            params.put("Protein",protein.getText().toString());
            params.put("Carb",carb.getText().toString());
            params.put("Calorie",calorie.getText().toString());
            params.put("Fat",fat.getText().toString());
            params.put("Foodtype",foodtype1);
            params.put("vnv",vn1);

            String url = "https://secure-tundra-65917.herokuapp.com/profile/newfood";
           // String url = "http://192.168.31.200:5000/";

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Process os success response
                            Log.e("Response ", response.toString());
                            //Toast.makeText(ProfileActivity.this, "Received"+response.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(insertFood.this, "Successful !", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error: ", error.toString());
                }
            });

            requestQueue.add(postRequest);
        }
            }
        });


    }

    private boolean validate()
    {
        boolean result=false;
        String a,b,c,d,e;
        a=fooditem.getText().toString();
        b=calorie.getText().toString();
        c=fat.getText().toString();
        d=protein.getText().toString();
        e=carb.getText().toString();

        if(a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || e.isEmpty() || vn1.isEmpty() || foodtype1.isEmpty())
        {
            Toast.makeText(insertFood.this,"Please Fill Complete Details !!",Toast.LENGTH_SHORT).show();
        }
        else {
            result=true;
        }
        return result;
    }
}
