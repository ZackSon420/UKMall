package com.example.ukmall;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ukmall.utils.model.Item;
import com.example.ukmall.viewmodel.CartViewModel;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

public class Receipt extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("0.00");
    private RecyclerView cartView;
    private CartViewModel cartViewModel;
    RecyclerView.LayoutManager cartLayoutManager;
    MakeOrderAdapter cartAdapter;
    Context context;
//    View view;
//    Bitmap bitmap, scaledbmp;


    //    private RecyclerView receiptview;
//    RecyclerView.LayoutManager receiptLayoutManager;
//    ReceiptAdapter receiptAdapter;
//    CartViewModel cartViewModel;
//    public List<Item> selectedProductList;
    TextView priceBought, TVBigPrice, TVtransacid, TVsubtotal;
    Button btnMenu;
//    int pageHeight = 1120;
//    int pageWidht = 792;

//    private static final int PERMISSION_REQUEST_CODE = 200;
    //
//    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        priceBought = findViewById(R.id.tv_pricebought);
        TVBigPrice = findViewById(R.id.tv_bigprice);
        TVtransacid = findViewById(R.id.tv_transactionid);
        TVsubtotal=findViewById(R.id.tv_purchase);
        btnMenu = findViewById(R.id.btn_mainmenu);
//
//        view = getWindow().getDecorView().getRootView();
//        view.setDrawingCacheEnabled(true);
//        bitmap = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
//        scaledbmp = Bitmap.createScaledBitmap(bitmap, 140,140, false);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        cartView = findViewById(R.id.rv_itembought);
        cartLayoutManager = new GridLayoutManager(this, 1);
        cartView.setLayoutManager(cartLayoutManager);
        cartAdapter = new MakeOrderAdapter(this);


        cartView.setAdapter(cartAdapter);
        cartView.setHasFixedSize(true);

        cartViewModel.getAllCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                cartAdapter.setItemCartList(items);
            }
        });

        Intent intent = getIntent();
        TVBigPrice.setText(df.format(intent.getDoubleExtra("totalPrice", 0)));
        TVsubtotal.setText(df.format(intent.getDoubleExtra("subtotal", 0.0)));
        priceBought.setText(df.format(intent.getDoubleExtra("totalPrice", 0)));
        TVtransacid.setText(intent.getStringExtra("orderID"));

//        if(!checkPermission()){
//            requestPermission();
//        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartViewModel.deleteAllCartItems();

                Intent intent = new Intent(Receipt.this, Homepage.class);
                startActivity(intent);
            }
        });

    }

//    private void generatePDF() {
//        PdfDocument pdfDocument = new PdfDocument();
//        String fileName = Environment.getExternalStorageDirectory()+"/receipt.pdf";
//
//        Paint paint = new Paint();
//        Paint title = new Paint();
//
//        PdfDocument.PageInfo receiptpageInfo = new PdfDocument.PageInfo.Builder(pageWidht,pageHeight,1).create();
//        PdfDocument.Page receiptPage = pdfDocument.startPage(receiptpageInfo);
//
//
//
//        Canvas canvas = receiptPage.getCanvas();
//
//        canvas.drawBitmap(scaledbmp,56,40,paint);
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, R.color.purple_700));
//        canvas.drawText("Test 1",209,100,title);
//        canvas.drawText("Test 2",209,80,title);
//
//        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
//        title.setTextSize(15);
//        title.setTextAlign(Paint.Align.CENTER);
//        canvas.drawText("This is sample document which we have created.", 396, 560, title);
//
//        pdfDocument.finishPage(receiptPage);
//
//        File file = new File(Environment.getExternalStorageDirectory(), "ReceiptTest.pdf");
//
//        try {
//            // after creating a file name we will
//            // write our PDF file to that location.
//            pdfDocument.writeTo(new FileOutputStream(file));
//
//            // below line is to print toast message
//            // on completion of PDF generation.
//            Toast.makeText(Receipt.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            // below line is used
//            // to handle error
//            e.printStackTrace();
//        }
//        // after storing our pdf to that
//        // location we are closing our PDF file.
//        pdfDocument.close();
//    }

//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//    }
//
//    private boolean checkPermission() {
//        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
//        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
//    }

}
