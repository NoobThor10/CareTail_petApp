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

        bellIcon = findViewById(R.id.imageView3);
        searchEditText = findViewById(R.id.editTextText);
        recyclerView = findViewById(R.id.recyclerView);
        ImageButton back = findViewById(R.id.imageButton2);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Shop.this, Dashboard.class);
            startActivity(intent);
            finish();
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Upload + show products
        uploadAndShowProducts();

        bellIcon.setOnClickListener(v ->
                Toast.makeText(Shop.this, "Bell icon clicked", Toast.LENGTH_SHORT).show()
        );

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void uploadAndShowProducts() {
        addAndShowProduct("Pedigree", "Best Dog food for dogs", "₹799", R.drawable._906002482832, 4.5f);
        addAndShowProduct("Fluffy Plush Calming Furry Tent House for Cats", "Soft cat bed", "₹3000", R.drawable.catsbed, 4.0f);
        addAndShowProduct("Rubber Koala", "Squeky Chewy toy for your pet", "₹299", R.drawable.dogtoy, 3.5f);
        addAndShowProduct("Me O Persian", "AntiHairfall Adult cat food", "₹499", R.drawable.catfood, 5.0f);
        addAndShowProduct("4 in 1 Shampoo", "Pet shampoo", "₹600", R.drawable.shampoopet, 4.2f);
    }

    private void addAndShowProduct(String name, String description, String price, int imageResId, float rating) {
        // Show on screen
        Product product = new Product(name, description, price, imageResId, rating);
        productList.add(product);
        productAdapter.notifyDataSetChanged();

        // Upload to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("description", description);
        data.put("price", price);
        data.put("image", imageResId);
        data.put("rating", rating);

        db.collection("products")
                .add(data)
                .addOnSuccessListener(docRef -> {
                    // optional success toast
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

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
