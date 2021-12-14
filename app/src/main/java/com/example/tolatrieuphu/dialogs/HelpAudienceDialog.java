package com.example.tolatrieuphu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HelpAudienceDialog {
    private final AlertDialog dialog;
    private BarChart chart;
    private Button audienceClose;
    private ArrayList<BarEntry> oleValues = new ArrayList<>();
    private ArrayList<Float> values = new ArrayList<>();
    private Handler handler = new Handler();
    private Runnable runnable;
    private int count;
    private int trueCase;


    public HelpAudienceDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.help_audience_dialog, null);
        chart = v.findViewById(R.id.audience_chart_bar);

        builder.setView(v);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        audienceClose = v.findViewById(R.id.audience_close_btn);
        audienceClose.setOnClickListener(v1 -> dismissDialog());
    }

    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.YELLOW);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(Constants.MAX_X_VALUE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0) {
                    if (Constants.ANS.length > (int) value) {
                        return Constants.ANS[(int) value];
                    } else return "";
                } else {
                    return "";
                }
            }
        });

        xAxis.setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setDrawBarShadow(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLimitLinesBehindData(false);

    }

    private BarData createChartData() {

        for (int i = 0; i < Constants.MAX_X_VALUE; i++) {
            float y = 0;
            oleValues.add(new BarEntry((float) i, y));
        }
        float remaining = Constants.MAX_Y_VALUE;
        for (int i = 0; i < Constants.MAX_X_VALUE; i++) {
            if (remaining > 0) {
                float y = nextFloatBetween2(Constants.MIN_Y_VALUE, remaining);
                remaining -= y;
                values.add(y);
            } else {
                values.add((float) 0);
            }

        }

        Collections.swap(values, values.indexOf(Collections.max(values)), trueCase-1);
        return null;
    }

    private void prepareChartData(BarData data) {
        chart.setData(data);
        chart.invalidate();
    }


    public static float nextFloatBetween2(float min, float max) {
        return (new Random().nextFloat() * (max - min)) + min;
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        dialog.setOnDismissListener(onDismissListener);
    }

    public void showDialog() {
        dialog.show();
        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);
        chart.animateY(Constants.TIME_DELAYED);
        animateNumber(oleValues, values);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                audienceClose.setVisibility(View.VISIBLE);
            }
        }, Constants.TIME_DELAYED);
    }

    public void animateNumber(ArrayList<BarEntry> oldValues, ArrayList<Float> values) {
        runnable = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<values.size(); i++) {

                    if (oldValues.get(i).getY() < values.get(i)) {
                        float increase = values.get(i) / (float) (Constants.TIME_DELAYED / 100);
                        oldValues.get(i).setY(oldValues.get(i).getY() + increase);

                    } else {
                        count += 1;
                    }
                }
                BarDataSet set1 = new BarDataSet(oldValues, "set1");
                set1.setColor(R.color.bar_color);
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                data.setValueTextSize(15f);
                data.setValueTextColor(Color.WHITE);
                data.setBarWidth(0.7f);
                data.setValueFormatter(new PercentFormatter());
                chart.setData(data);
                chart.notifyDataSetChanged();
                chart.invalidate();
                if (count < 4) {
                    handler.postDelayed(runnable, 100);
                }
            }
        };
        handler.post(runnable);
    }

    public void setTrueCase(int trueCase) {
        this.trueCase = trueCase;
    }

}
