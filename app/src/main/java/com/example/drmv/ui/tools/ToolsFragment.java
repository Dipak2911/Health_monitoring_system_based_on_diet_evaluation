package com.example.drmv.ui.tools;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.drmv.HomeActivity2;
import com.example.drmv.R;
import com.example.drmv.ui.home.HomeFragment;
import com.example.drmv.ui.share.ShareFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    Float consumed=0f,target=0f;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

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
                        consumed=Float.parseFloat(l.get(0).get("Consumed").toString());
                        target=Float.parseFloat(l.get(0).get("Target").toString());
                        //+================+++++++++++++++++++++++++++++++++++++++
                        if(consumed>target)
                        {
                            textView.setText("oops !! u have exceeded your target ...\n Don't worry we will take care !");
                            textView.setTextColor(Color.RED);
                        }
                        else if (consumed<target)
                        {
                            textView.setText("You havent Reached your target yet !");
                            textView.setTextColor(Color.GREEN);
                        }
                        else if(consumed==target)
                        {
                            textView.setText("Yay, You reached goal !");
                            textView.setTextColor(Color.GREEN);
                        }

                        Cartesian cartesian = AnyChart.column();
                        List<DataEntry> data = new ArrayList<>();
                        data.add(new ValueDataEntry("Consumed", consumed));
                        data.add(new ValueDataEntry("Target", target));


                        Column column = cartesian.column(data);
                        column.tooltip()
                                .titleFormat("{%X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0d)
                                .offsetY(5d)
                                .format("Calories : {%Value}{groupsSeparator: }");


                        cartesian.background().stroke("3 black");
                        cartesian.animation(true);
                        cartesian.title("Todays Consumed Calories Against Target Calries");

                        cartesian.yScale().minimum(0d);

                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                        cartesian.interactivity().hoverMode(HoverMode.BY_X);

                        cartesian.yAxis(0).title("Calories");

                        AnyChartView anyChartView = (AnyChartView) root.findViewById(R.id.bar_chart);
                        anyChartView.setChart(cartesian);
                        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    }
                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                }
        );








        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText();
            }
        });
        return root;
    }
}