package com.example.drmv.ui.share;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.drmv.ProfileActivity2;
import com.example.drmv.R;
import com.example.drmv.insertFood;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;

    private FirebaseAuth firebaseAuth;
    private TextView bf1,dn1,ln1;
    static public Float target_cal;
    private FirebaseUser firebaseUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

       final TextView tv=root.findViewById(R.id.tv);
       bf1=root.findViewById(R.id.bf_nm);
        dn1=root.findViewById(R.id.dn_nm);
        ln1=root.findViewById(R.id.ln_nm);
       firebaseAuth=FirebaseAuth.getInstance();
       firebaseUser=firebaseAuth.getCurrentUser();


/*
        String URL="https://secure-tundra-65917.herokuapp.com/api/6";
        final RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Rest Response",response.toString());
                tv.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest response",error.toString());
            }
        });

        requestQueue.add(objectRequest);

 */
//========================================================================================
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Uid",firebaseAuth.getUid());


        String url = "https://secure-tundra-65917.herokuapp.com/profile/dietchart";
        //String url = "http://192.168.31.200:5000/";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Process os success response
                        Log.e("Response ", response.toString());
                        //Toast.makeText(ProfileActivity.this, "Received"+response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            bf1.setText(response.get("Breakfast").toString().replaceAll("[^a-zA-Z0-9,]", " "));
                            ln1.setText(response.get("Lunch").toString().replaceAll("[^a-zA-Z0-9,]", " "));
                            dn1.setText(response.get("Dinner").toString().replaceAll("[^a-zA-Z0-9,]", " "));
                            tv.setText(response.get("Day").toString());

                            Float tt=Float.parseFloat(response.get("Totcal").toString());
                            //====================================================
                            HashMap testage = new HashMap<>();
                            testage.put("Target",tt);
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
                            //  target_cal=Float.parseFloat(response.get("Totcal").toString());
                            Toast.makeText(getActivity(), "Successful !", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.toString());
                Toast.makeText(getActivity(), "Error !", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(postRequest);

        //==============================================================================================
        final TextView textView = root.findViewById(R.id.text_share);
        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}