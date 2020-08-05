package com.example.drmv.ui.home;
import java.lang.Object;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.example.drmv.HomeActivity2;
import com.example.drmv.MainActivity;
import com.example.drmv.ProfileActivity;
import com.example.drmv.ProfileActivity2;
import com.example.drmv.R;
import com.example.drmv.insertFood;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseMessaging firebaseMessaging;
    TextView txvResult;
    private View view;
    ImageView iconn;
    String food_stment;
    Button insertdb,senddb ,be,done;
    EditText fff;
    static public Float daily_cal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        txvResult=root.findViewById(R.id.txvResult);
        insertdb=root.findViewById(R.id.button3);
        senddb=root.findViewById(R.id.senddb);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        iconn=root.findViewById(R.id.btnSpeak);
        be=root.findViewById(R.id.buttone);
        done=root.findViewById(R.id.done);
        fff=root.findViewById(R.id.food_edit);
        fff.setVisibility(View.INVISIBLE);
        done.setVisibility(View.INVISIBLE);
        senddb.setEnabled(false);


        FirebaseMessaging.getInstance().subscribeToTopic("Health");

        be.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fff.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
                fff.setText(txvResult.getText());

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvResult.setText(fff.getText());
                food_stment=fff.getText().toString();
                fff.setVisibility(View.INVISIBLE);
                done.setVisibility(View.INVISIBLE);
            }
        });
//====================================================================================================================
        iconn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(getActivity(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }

            }
        });
//====================================================================================================================
        insertdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final RequestQueue requestQueue=Volley.newRequestQueue(getActivity().getApplicationContext());
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Uid",firebaseAuth.getUid());
                params.put("food",food_stment);


                String url = "https://secure-tundra-65917.herokuapp.com/profile/insert";

                //String url = "http://192.168.31.200:5000/";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Process os success response
                                try {
                                    if(response.get("Status").toString().equals("Unsuccessful"))
                                    {
                                        senddb.setEnabled(true);
                                        Toast.makeText(getActivity(),"We Can't Find Food Item !!\n You Can Add it to Database", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getActivity(),"Received : "+response.get("Food"), Toast.LENGTH_SHORT).show();
                                        //daily_cal=Float.parseFloat(response.get("CalNow").toString());

                                        Float cc=Float.parseFloat(response.get("CalNow").toString());;
                                        //====================================================
                                        HashMap testage = new HashMap<>();
                                        testage.put("Consumed",cc);

                                        final String where="Email='"+firebaseUser.getEmail()+"'";

                                        Backendless.Data.of("userinfo").update(where, testage, new AsyncCallback<Integer>() {
                                            @Override
                                            public void handleResponse(Integer response) {
                                                // Toast.makeText(ProfileActivity2.this,"Saved",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                //Toast.makeText(getActivity(),where,Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //==============================
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.toString());
                    }
                });

                requestQueue.add(postRequest);
            }

        });
//====================================================================================================================
        senddb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),insertFood.class));
            }
        });
//====================================================================================================================
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String select="Email='"+firebaseUser.getEmail()+"'";
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause( select );
                final ArrayList<Map> l=new ArrayList<Map>();
                Backendless.Data.of("userinfo").find(
                        queryBuilder, new AsyncCallback<List<Map>>() {
                            @Override
                            public void handleResponse(List<Map> response) {
                                l.add(response.get(0));
                                textView.setText("Welcome \n"+l.get(0).get("Name").toString());

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                            }
                        }
                );
               //textView.setText("Welcome \n"+firebaseUser.getEmail());
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));
                    food_stment=txvResult.getText().toString();
                }
                break;
        }
    }

}