package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ukmall.utils.model.Item;
import com.example.ukmall.viewmodel.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/*

    THINGS TO DO
    - Resize button
    - Check position all component on different screen size

 */
public class ProductDetails extends AppCompatActivity implements View.OnClickListener {


    //private static final String KEY_PRODUCT_NAME = "name";
    //private static final String KEY_PRODUCT_DESC = "description";
    //private static final String KEY_STORE_NAME = "name";

    //declare variable
    TextView tvProductID, tvStoreName, tvProductName, tvProductPrice, tvProductDesc, tvQuantity;
    Button btnAdd, btnMinus, btnAddToCart, btnBuyNow, btnViewProduct;
    private ImageSlider imageSlider;
    String img, img2;
    Integer quantity = 1;
    private List<Item> itemCartList;
    private CartViewModel viewModel;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    //Retrieve data manually
    /*FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference productRef = db.document("/store/Q9oYbWZLA5Mj7CBee5kK/product/testproduct");
    DocumentReference storeRef = db.document("/store/Q9oYbWZLA5Mj7CBee5kK");
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        tvProductID = findViewById(R.id.tv_ProductID);
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
        itemCartList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        //get data from RecyclerView
        Intent intent = getIntent();

        tvProductID.setText(intent.getStringExtra("productId"));
        tvProductName.setText(intent.getStringExtra("productName"));
        tvProductDesc.setText(intent.getStringExtra("productDesc"));
        tvProductPrice.setText(intent.getStringExtra("productPrice"));
        tvStoreName.setText(intent.getStringExtra("store"));
        img = intent.getStringExtra("productImage");
        img2 = intent.getStringExtra("productImage2");

        //add image to imageSlider
        slideModels.add(new SlideModel(img,ScaleTypes.FIT));
        //slideModels.add(new SlideModel(img2, ScaleTypes.CENTER_INSIDE));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


        viewModel.getAllCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> itemCarts) {
                itemCartList.addAll(itemCarts);
            }
        });


        // BottomNavigationView
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);

        // Utk set Home as default
        bottomNavigationView.setSelectedItemId(R.id.home);


        // Perform item selected listener untuk button BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        Toast.makeText(ProductDetails.this, "Homepage", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.addtocart:
                        startActivity(new Intent(getApplicationContext(), Cart.class));
                        Toast.makeText(ProductDetails.this, "Add to Cart", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.addproduct:
                        startActivity(new Intent(getApplicationContext(), Add_Product.class));
                        Toast.makeText(ProductDetails.this, "Add Product", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(),Analytics.class));
                        Toast.makeText(ProductDetails.this, "Analytics", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });

    }

    private void insertToRoom() {

        Item item = new Item();
        Intent intent = getIntent();

        item.setItemName(intent.getStringExtra("productName"));
        item.setProductID(intent.getStringExtra("productId"));

       // item.setShoeBrandName(shoe.getShoeBrandName());
        String prd= intent.getStringExtra("productPrice");
        String str = prd.substring(2);

        item.setItemPrice(Double.parseDouble(str));
        item.setItemImage(intent.getStringExtra("productImage"));

        int num = Integer.parseInt((String) tvQuantity.getText());
        int num2 = quantity;
        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!itemCartList.isEmpty()){
            for(int i=0;i<itemCartList.size();i++){
                if (item.getItemName().equals(itemCartList.get(i).getItemName())){
                    quantity[0] = itemCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = itemCartList.get(i).getItemID();
                }
            }
        }
        quantity[0] = num2;

        if (quantity[0]==1||quantity[0]>1){
            item.setQuantity(quantity[0]);
            item.setTotalItemPrice(quantity[0]*item.getItemPrice());
            viewModel.insertCartItem(item);
        }else{

            viewModel.updateQuantity(id[0] ,quantity[0]);
            viewModel.updatePrice(id[0] , quantity[0]*item.getItemPrice());
        }

        //Restrict User From Accessing Same Cart------------------------------------------------------------------
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        //Push User Data on Cart to Firebased Named "AddToCart"
        final HashMap<String, Object> cartMap = new HashMap<>();


        cartMap.put("productId", tvProductID.getText().toString());
        cartMap.put("productName",tvProductName.getText().toString());
        cartMap.put("productPrice",tvProductPrice.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);




        /*cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",tvQuantity.getText().toString());
        cartMap.put("totalPrice",tvProductPrice);*/


        db.collection("AddToCart").document("CUS"+mAuth.getCurrentUser().getUid()).collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                finish();
            }
        });

        startActivity(new Intent(ProductDetails.this , Cart.class));

//----------------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add: {
                quantity++;
                tvQuantity.setText(quantity.toString());
                break;
            }

            case R.id.btn_minus: {
                if (quantity >= 1)
                    quantity--;
                else
                    quantity = 0;
                tvQuantity.setText(quantity.toString());
                break;
            }

            //button untuk fetch data
            /*case R.id.btn_fetch_data: {
                //loadProduct();
                break;
            }
             */

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
        if(quantity > 0)
            Toast.makeText(ProductDetails.this, "Buy Now!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ProductDetails.this, "Please select at least 1 quantity", Toast.LENGTH_SHORT).show();
        //akan proceed to payment page
    }

    //function add to cart
    private void addToCart() {


        if(quantity > 0) {
            insertToRoom();
            Toast.makeText(ProductDetails.this, "Item were added to cart", Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(ProductDetails.this, "Please select at least 1 quantity", Toast.LENGTH_SHORT).show();

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