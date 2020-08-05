package com.example.drmv.ui.send;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.drmv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView histtext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_send, container, false);

        firebaseAuth=FirebaseAuth.getInstance();

        histtext=root.findViewById(R.id.histtext);

       RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Uid",firebaseAuth.getUid());

       // String url = "http://192.168.31.200:5000/";
        String url = "https://secure-tundra-65917.herokuapp.com/profile/food";
//
        final ArrayList<Float> cal=new ArrayList<Float>();
        final ArrayList<String> fooditem=new ArrayList<String>();
        final JsonObject ob=new JsonObject();
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Process os success response
                        Pie pie = AnyChart.pie();
                        List<DataEntry> data1=new ArrayList<>();
                       // Log.e("Response ", response.toString());
                        Iterator<String> it=response.keys();
                        while (it.hasNext())
                        {
                            String key=it.next();

                            try {
                                    fooditem.add(key);

                                    cal.add(Float.parseFloat(response.get(key).toString()));

                                    data1.add(new ValueDataEntry(key, Float.parseFloat(response.get(key).toString())));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                        }
                        if(fooditem.get(0).equals("null"))
                        {
                            Toast.makeText(getActivity(), "No Data !", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            for(int i=0;i<fooditem.size();i++)
                            {
                                histtext.setText(histtext.getText() +"\n"+fooditem.get(i)+" : "+cal.get(i));
                            }

                            pie.background().stroke("3 black");
                            pie.animation(true);
                            pie.data(data1);
                            AnyChartView anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);
                            anyChartView.setChart(pie);
                        }
                        //Toast.makeText(getActivity(), "Received : "+fooditem.get(2), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.toString());
            }
        });

        requestQueue.add(postRequest);
/*
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));

        pie.data(data);

        AnyChartView anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);


 */
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
}

