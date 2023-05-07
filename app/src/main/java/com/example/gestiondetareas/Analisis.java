package com.example.gestiondetareas;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Analisis extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private BarChart barChart;
    private BarDataSet barDataSet_1;
    private BarDataSet barDataSet_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analisis);

        barChart = findViewById(R.id.barChart);


// Creamos un set de datos
        ArrayList<BarEntry> barEntries_1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> barEntries_2 = new ArrayList<BarEntry>();
        ArrayList<String> labels = new ArrayList<>();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String correo= currentUser.getEmail();
        HashMap<String, float[]> dicc = new HashMap<>();
        db.collection("tareas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                queryDocumentSnapshots.forEach(d -> {

                    if(d.getId().contains(correo)){

                        if(dicc.containsKey(d.get("categoriaTarea").toString())){
                            if(d.get("estado").toString().contains("true")){
                                float j = (float) dicc.get(d.get("categoriaTarea").toString())[0]+1;
                                float[] result= dicc.get(d.get("categoriaTarea").toString());
                                result[0] = j;
                                dicc.put(d.get("categoriaTarea").toString(),result);
                            }else{
                                float j = dicc.get(d.get("categoriaTarea").toString())[1]+1;
                                float[] result= dicc.get(d.get("categoriaTarea").toString());
                                result[1] = j;
                                dicc.put(d.get("categoriaTarea").toString(),result);
                            }

                        }else{
                            if(d.get("estado").toString().contains("true")){

                                float[] result= new float[]{1,0};


                                dicc.put(d.get("categoriaTarea").toString(),result);
                            }else{

                                float[] result= new float[]{0,1};

                                dicc.put(d.get("categoriaTarea").toString(),result);
                            }
                        }


                    }
                });
                Float j = Float.valueOf(0);
                System.out.println(dicc);
                for (String key: dicc.keySet()) {
                    barEntries_1.add(new BarEntry(j, dicc.get(key)[0]));
                    barEntries_2.add(new BarEntry(j, dicc.get(key)[1]));
                    labels.add(key);
                    j++;
                }

                barDataSet_1 = new BarDataSet(barEntries_1, "En curso");
                barDataSet_2 = new BarDataSet(barEntries_2, "Finalizadas");
                barDataSet_1.setColor(Color.parseColor("#ff5232"));
                barDataSet_2.setColor(Color.parseColor("#48a259"));
                barDataSet_1.setValueTextColor(Color.WHITE);

                barDataSet_2.setValueTextColor(Color.WHITE);
// Asociamos al gráfico
                BarData barData = new BarData(barDataSet_1,barDataSet_2);


                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);


                xAxis.setTextColor(Color.WHITE);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                Legend legend = barChart.getLegend();
                legend.setTextColor(Color.WHITE);
                YAxis yAxisLeft = barChart.getAxisLeft();
                yAxisLeft.setDrawGridLines(true);

                YAxis yAxisRight = barChart.getAxisRight();
                yAxisRight.setEnabled(false);
                barChart.getAxisLeft().setAxisMinimum(0f); // posición mínima del eje Y en 0
                 // posición máxima del eje Y en 100
                barChart.getAxisLeft().setDrawZeroLine(true);
                barChart.getAxisLeft().setTextColor(Color.WHITE);
                yAxisRight.setTextColor(Color.WHITE);
                barChart.getDescription().setEnabled(false);
                barChart.setData(barData);

                barChart.invalidate();
            }


        });

        ImageView analisis = (ImageView) findViewById(R.id.imageButton2);

        analisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Analisis.this, Analisis.class);
                startActivity(intent);
            }
        });

        ImageView perfil = (ImageView) findViewById(R.id.btnProfile);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Analisis.this,PerfilActivity.class);
                startActivity(intent);
            }
        });

        ImageView home = (ImageView) findViewById(R.id.button2);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Analisis.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        ImageView nueva = (ImageView) findViewById(R.id.button3);

        nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Analisis.this, Nueva_tarea_Activity.class);
                startActivity(intent);
            }
        });
    }
}
