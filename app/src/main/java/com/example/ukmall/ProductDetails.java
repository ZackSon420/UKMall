package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private static final String KEY_PRODUCT_NAME = "name";
    private static final String KEY_PRODUCT_DESC = "description";
    private static final String KEY_STORE_NAME = "name";

    // Study cara retrieve data from integer
    // private static final String KEY_PRODUCT_PRICE = "price";

    //declare variable
    TextView tvStoreName, tvProductName, tvProductPrice, tvProductDesc, tvQuantity;
    Button btnAdd, btnMinus, btnAddToCart, btnBuyNow, btnViewProduct;
    private ImageSlider imageSlider;
    Integer quantity = 1;

    //declare location firestore & cari cara nak locate based on selected product from recycler view
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference productRef = db.document("/store/Q9oYbWZLA5Mj7CBee5kK/product/testproduct");
    DocumentReference storeRef = db.document("/store/Q9oYbWZLA5Mj7CBee5kK");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        tvStoreName = findViewById(R.id.tv_store_name);
        tvProductName = findViewById(R.id.tv_productName);
        tvProductPrice = findViewById(R.id.tv_productPrice);
        tvProductDesc = findViewById(R.id.tv_productDesc);
        tvQuantity = findViewById(R.id.tv_quantity);
        imageSlider = findViewById(R.id.image_slider);

        btnAdd = findViewById(R.id.btn_add);
        btnMinus = findViewById(R.id.btn_minus);
        btnViewProduct = findViewById(R.id.btn_fetch_data);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnBuyNow = findViewById(R.id.btn_buy_now);

        btnAdd.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnViewProduct.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);

        Intent intent = getIntent();
        tvProductName.setText(intent.getStringExtra("productName").toString());
        //tvStoreName.setText(intent.getStringExtra("productName").toString());
        tvProductPrice.setText(intent.getStringExtra("productPrice").toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add: {
                addQuantity();
                break;
            }

            case R.id.btn_minus: {
                minusQuantity();
                break;
            }

            case R.id.btn_fetch_data: {
                //loadProduct();
                break;
            }

            case R.id.btn_add_to_cart: {
                addToCart();
                break;
            }

            case R.id.btn_buy_now: {
                buyNow();
                break;
            }
        }
    }

    private void buyNow() {
        Toast.makeText(ProductDetails.this, "Buy Now!", Toast.LENGTH_SHORT).show();
        //akan proceed to payment page
    }

    //function add to cart
    private void addToCart() {
        Toast.makeText(ProductDetails.this, "Item were added to cart", Toast.LENGTH_SHORT).show();

    }

    private void minusQuantity() {
        if (quantity >= 1)
            quantity--;
        else
            quantity = 0;
        tvQuantity.setText(quantity.toString());
    }

    private void addQuantity() {
        quantity++;
        tvQuantity.setText(quantity.toString());
    }


    //display all product details when button clicked
    /*private void loadProduct () {
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
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    slideModels.add(new SlideModel(queryDocumentSnapshot.getString("url"), ScaleTypes.FIT));
                                    imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                                }

                            } else {
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

     */
}