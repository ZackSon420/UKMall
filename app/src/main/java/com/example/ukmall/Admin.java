package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Admin extends AppCompatActivity implements View.OnClickListener{

    //Declare Recycler View

    private LineChart lineChart;
    private RecyclerView topSellerRV;
    RecyclerView.LayoutManager layoutManager;
    AdminAdapter adminAdapter;
    FirebaseFirestore db;
    ArrayList<user> sellerArrayList;
    TextView tvTotalUser, tvTotalProduct, tvTotalOrder, tvTotalSales;
    Double totalSales = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tvTotalUser = findViewById(R.id.tv_totalUser);
        tvTotalOrder = findViewById(R.id.tv_totalOrder);
        tvTotalProduct = findViewById(R.id.tv_totalProduct);
        tvTotalSales = findViewById(R.id.tv_totalSales);

        topSellerRV = findViewById(R.id.rv_seller);
        layoutManager=new GridLayoutManager(this, 1);
        topSellerRV.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        sellerArrayList = new ArrayList<user>();
        adminAdapter = new AdminAdapter(Admin.this, sellerArrayList);

        CollectionReference productCollection = db.collection("product");
        CollectionReference orderCollection = db.collection("order");
        CollectionReference userCollection = db.collection("user");

        AggregateQuery countProductQuery = productCollection.count();
        AggregateQuery countOrderQuery = orderCollection.count();
        AggregateQuery countSellerQuery = userCollection.count();

        countProductQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                tvTotalProduct.setText("" + snapshot.getCount());
            }
        });

        countOrderQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                tvTotalOrder.setText("" + snapshot.getCount());
            }
        });

        countSellerQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                tvTotalUser.setText("" + snapshot.getCount());
            }
        });

        getTotalSales();

        topSellerRV.setAdapter(adminAdapter);
        EventChangeListener();
        topSellerRV.setHasFixedSize(true);

        //Line Chart
        lineChart = findViewById(R.id.activity_main_linechart);
        //periodRadioGroup = findViewById(R.id.activity_main_period_radiogroup);
        //intervalRadioGroup = findViewById(R.id.activity_main_priceinterval);
        LineDataSet lineDataSet = new LineDataSet(dataValues(),"Total Sale");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        configureLineChart();

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
        //getSalesData();

    }

    private void getSalesData() {
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("Total Price");
        desc.setTextSize(10);
        lineChart.setDescription(desc);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
    }

    private ArrayList<Entry> dataValues() {
        {
            ArrayList<Entry> dataVals = new ArrayList<Entry>();
            dataVals.add(new Entry(0, 5));
            dataVals.add(new Entry(1, 12));
            dataVals.add(new Entry(2, 19));
            dataVals.add(new Entry(3, 20));
            return dataVals;
        }
    }

    private void getTotalSales() {

        db.collectionGroup("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Double totalPrice = document.getDouble("totalPrice");
                        totalSales += totalPrice;
                    }
                }else{
                    Log.d("debug", "Error");
                }
                tvTotalSales.setText(String.valueOf("RM" + totalSales));
            }
        });
    }

    private void EventChangeListener() {

        db.collectionGroup("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            sellerArrayList.add(dc.getDocument().toObject(user.class));

                            Collections.sort(sellerArrayList, new Comparator<user>() {
                                @Override
                                public int compare(user s1, user s2) {

                                    if (s1.totalSales > s2.totalSales) {
                                        return -1;
                                    }
                                    else if (s1.totalSales < s2.totalSales) {
                                        return 1;
                                    }
                                    else {
                                        return 0;
                                    }
                                }
                            });
                        }
                    }
                    adminAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

}