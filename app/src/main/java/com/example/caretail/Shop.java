package com.example.caretail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caretail.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop extends AppCompatActivity {

    private ImageView bellIcon;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Initialize views
        bellIcon = findViewById(R.id.imageView3);  // Bell icon ID
        searchEditText = findViewById(R.id.editTextText);  // Search bar ID
        recyclerView = findViewById(R.id.recyclerView);
        ImageButton back=findViewById(R.id.imageButton2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shop.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });



        // ✅ Use GridLayoutManager for 2-column grid
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up RecyclerView
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Load products only once
        if (productList.isEmpty()) {
            loadDummyProducts();
        }

        // Bell icon click listener
        bellIcon.setOnClickListener(v -> {
            Toast.makeText(Shop.this, "Bell icon clicked", Toast.LENGTH_SHORT).show();
        });

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Dummy product data
    private void loadDummyProducts() {
        productList.add(new Product("Pedigree", "Best Dog food for dogs", "₹799", R.drawable._906002482832, 4.5f));
        productList.add(new Product(" Fluffy Plush Calming Furry Tent House for Cats", "Soft cat bed", "₹3000", R.drawable.catsbed, 4.0f));
        productList.add(new Product("Rubber Koala", " Squeky Chewy toy for your pet", "₹299", R.drawable.dogtoy, 3.5f));
        productList.add(new Product(" Me O Persian", "AntiHairfall Adult cat food", "₹499", R.drawable.catfood, 5.0f));
        productList.add(new Product("4 in 1 Shampoo", "Pet shampoo", "₹600", R.drawable.shampoopet, 4.2f));

        productAdapter.notifyDataSetChanged();
    }

    private void addProductToFirestore(String name, String description, String price, int imageResId, float rating) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("description", description);
        product.put("price", price);
        product.put("image", imageResId);  // Optional: you can store image URL if using Firebase Storage
        product.put("rating", rating);

        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    // Only update local list once to avoid duplicates
                    productList.add(new Product(name, description, price, imageResId, rating));
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // Search filter
    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }
}
