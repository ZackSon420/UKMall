package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*

    THINGS TO DO
    - Study cara display product on selected item
    - Cari cara locate product from firestore
    - Add functionality quantity button
    - Resize button
    - Check position all component on different screen size

 */
public class ProductDetails extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProductDetails";

    private static final String KEY_PRODUCT_NAME = "name";
    private static final String KEY_PRODUCT_DESC = "description";
    private static final String KEY_STORE_NAME = "name";

    // Study cara retrieve data from integer
    // private static final String KEY_PRODUCT_PRICE = "price";

    //declare variable
    TextView tvStoreName, tvProductName, tvProductPrice, tvProductDesc;
    private ImageSlider imageSlider;

    //declare location firestore & cari cara nak locate based on selected product from recycler view
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference productRef = db.document("/store/Q9oYbWZLA5Mj7CBee5kK/product/testproduct");
    DocumentReference storeRef = db.document("/store/Q9oYbWZLA5Mj7CBee5kK");
    //DocumentReference productimgRef = db.document("/store/Q9oYbWZLA5Mj7CBee5kK/product/testproduct/images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        tvStoreName = findViewById(R.id.tv_store_name);
        tvProductName = findViewById(R.id.tv_productName);
        tvProductPrice = findViewById(R.id.tv_productPrice);
        tvProductDesc = findViewById(R.id.tv_productDesc);
        imageSlider = findViewById(R.id.image_slider);

    }

    @Override
    public void onClick(View view) {

    }

    //display all product details when button clicked
    public void loadProduct (View v){

        //list of images
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        //get details in product document
        productRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String pName = documentSnapshot.getString(KEY_PRODUCT_NAME);
                            String pDesc = documentSnapshot.getString(KEY_PRODUCT_DESC);
                            //String pPrice = documentSnapshot.getString(KEY_PRODUCT_PRICE);
                            //String pImage = documentSnapshot.getString(KEY_PRODUCT_IMAGE);
                            //String sName = documentSnapshot.getString(KEY_STORE_NAME);

                            //Map<String, Object> product = documentSnapshot.getData();
                            tvProductName.setText(pName);
                            tvProductDesc.setText(pDesc);
                            //tvProductPrice.setText(pPrice);
                            //ivProduct.setImageResource(pImage);
                            //tvStoreName.setText(sName);
                        } else {
                            Toast.makeText(ProductDetails.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetails.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        //get store name (try look for easier way later)
        storeRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String sName = documentSnapshot.getString(KEY_STORE_NAME);

                            tvStoreName.setText(sName);
                        } else {
                            Toast.makeText(ProductDetails.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetails.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        //get image to put inside image slider
        db.collection("imageTest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            // check if task success
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        slideModels.add(new SlideModel(queryDocumentSnapshot.getString("url"), ScaleTypes.FIT));
                        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                    }

                }
                else{
                    Toast.makeText(ProductDetails.this, "Cant load Images", Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetails.this, "FAIL : Cant load Images", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}