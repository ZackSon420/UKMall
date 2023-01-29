package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity  {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference productRef;
    ImageView productIv;
    EditText titleEt;
    EditText descriptionEt;
    EditText categoryEt;
    EditText quantityEt;
    EditText priceEt;
    TextView prodIdEt;
    Button updatebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);


        prodIdEt = findViewById(R.id.productid);
        productIv = findViewById(R.id.productIconTV);
        titleEt = findViewById(R.id.titleEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        categoryEt = findViewById(R.id.categoryEt);
        quantityEt = findViewById(R.id.quantityEt);
        priceEt = findViewById(R.id.priceEt);

        updatebtn = findViewById(R.id.updateproductBtn);

        String productID = getIntent().getStringExtra("productId");
        String url = getIntent().getStringExtra("url");
        String productName = getIntent().getStringExtra("productTitle");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productCategory = getIntent().getStringExtra("productCategory");
        int productQuantity = getIntent().getIntExtra("productQuantity", 0);
        double productPrice = getIntent().getDoubleExtra("productPrice", 0);


        prodIdEt.setText(productID);
        Picasso.get().load(url).into(productIv);
        titleEt.setText(productName);
        descriptionEt.setText(productDescription);
        categoryEt.setText(productCategory);
        quantityEt.setText(String.valueOf(productQuantity));
        priceEt.setText(String.valueOf(productPrice));



        //creating a reference to the document
        productRef = db.collection("product").document(productID);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEt.getText().toString();
                String description = descriptionEt.getText().toString();
                String category = categoryEt.getText().toString();
                int quantity = Integer.parseInt(quantityEt.getText().toString());
                double price = Double.parseDouble(priceEt.getText().toString());

                Map<String, Object> product = new HashMap<>();
                product.put("title", title);
                product.put("description", description);
                product.put("category", category);
                product.put("quantity", quantity);
                product.put("price", price);

                productRef.update(product)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProductActivity.this, "Error updating product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}



