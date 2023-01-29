package com.example.ukmall;


import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;



import java.io.File;
import java.util.HashMap;

import io.grpc.Metadata;

public class Add_Product extends AppCompatActivity {

    private ImageView productIconTV;
    private EditText titleEt, descriptionEt, categoryEt, quantityEt, priceEt;
    private Button addProductBtn;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;
    //picked image Uri
    private Uri image_uri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    //private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productIconTV = findViewById(R.id.productIconTV);
        titleEt = findViewById(R.id.titleEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        categoryEt = findViewById(R.id.categoryEt);
        quantityEt = findViewById(R.id.quantityEt);
        priceEt = findViewById(R.id.priceEt);
        addProductBtn = findViewById(R.id.addproductBtn);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //init permission arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){

                image_uri = result.getData().getData();
                productIconTV.setImageURI(image_uri);

        }

            }
        });
        productIconTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog to pick image
                showImageDialog();

            }
        });
        categoryEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick category
                categoryDialog();
            }
        });
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
    }

    private String productTitle, productDescription, productCategory,productQuantityStr, productPriceStr;
    private Integer productQuantity;
    private Double originalPrice;
    private void inputData() {
        productTitle=titleEt.getText().toString().trim();
        productDescription=descriptionEt.getText().toString().trim();
        productCategory=categoryEt.getText().toString().trim();
        productQuantityStr=quantityEt.getText().toString().trim();
        productQuantity=Integer.valueOf(productQuantityStr);
        productPriceStr=priceEt.getText().toString();
        originalPrice=Double.valueOf(String.valueOf(productPriceStr));

        if(TextUtils.isEmpty(productTitle)){
            Toast.makeText(this, "Title is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productDescription)){
            Toast.makeText(this, "Description is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productCategory)){
            Toast.makeText(this, "Category is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productQuantityStr)){
            Toast.makeText(this, "Quantity is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productPriceStr)){
            Toast.makeText(this, "Price is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        addProduct();

    }

    private void addProduct() {
        String timestamp = "" + System.currentTimeMillis();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String productid = "PROD"+timestamp;

        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap();
            hashMap.put("productId", productid);
            hashMap.put("productTitle", "" + productTitle);
            hashMap.put("productDescription", "" + productDescription);
            hashMap.put("productCategory", "" + productCategory);
            hashMap.put("productQuantity", productQuantity);
            hashMap.put("url", "https://firebasestorage.googleapis.com/v0/b/ukmall-f47b3.appspot.com/o/product_images%2FOIP.jpg?alt=media&token=c401a8ef-f83b-4ce9-ba3b-48a432b05e2d"); // set path for no image file
            hashMap.put("originalPrice", originalPrice);


            db.collection("product").document(productid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    // The product was successfully added to the database
                    Toast.makeText(Add_Product.this, "Product Added Succesfully", Toast.LENGTH_SHORT).show();
                    clearData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // There was an error adding the product to the database
                    Toast.makeText(Add_Product.this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            //  FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Get a reference to the storage bucket
            String FileNameAndPath= "product_images/"+""+timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(FileNameAndPath);

            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUrl= uriTask.getResult();

                            if(uriTask.isSuccessful()){
                                HashMap<String, Object> hashMap = new HashMap();
                                hashMap.put("productId", productid);
                                hashMap.put("productTitle",""+ productTitle);
                                hashMap.put("productDescription",""+productDescription);
                                hashMap.put("productCategory", ""+productCategory);
                                hashMap.put("productQuantity",productQuantity);
                                hashMap.put("url", ""+downloadImageUrl);
                                hashMap.put("originalPrice", originalPrice);

                                db.collection("product").document(productid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // The product was successfully added to the database
                                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                                        Toast.makeText(Add_Product.this, "Product Added Succesfully", Toast.LENGTH_SHORT).show();
                                        clearData();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // There was an error adding the product to the database
                                        Toast.makeText(Add_Product.this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add_Product.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void clearData() {
        titleEt.setText("");
        descriptionEt.setText("");
        categoryEt.setText("");
        quantityEt.setText("");
        priceEt.setText("");
        productIconTV.setImageResource(R.drawable.ic_baseline_add_primary);
        image_uri=null;
    }


    private void categoryDialog() {
        //dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(Constants.productCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = Constants.productCategories[i];
                    categoryEt.setText(category);
                    }
                })
                .show();
    }

    public void showImageDialog() {
            String [] options={"Gallery"};
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Pick Image")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                          if(which==0){
                              if(checkStoragePermission()){
                                  //Permission Granted
                                  pickFromGallery();
                              }
                              else{
                                  //Permission Not Granted, Request Permission
                                  requestStoragePermission();
                              }
                          }

                        }
                    })
                    .show();

       }


        private void pickFromGallery(){
            //Intent to pick image from gallery

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
       // startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
            activityResultLauncher.launch(intent);
        }

        private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
        }
        private void requestStoragePermission(){
            ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
        }

        //handle permission results
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch(requestCode){
//
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

        //handle image pick results
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(resultCode==RESULT_OK){
//            if(requestCode==IMAGE_PICK_GALLERY_CODE)
//            {
//                image_uri = data.getData();
//                productIconTV.setImageURI(image_uri);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}







