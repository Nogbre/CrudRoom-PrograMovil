package com.example.crudroom;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudroom.model.Product;
import com.example.crudroom.viewmodel.ProductViewModel;

public class MainActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            adapter.setProducts(products); // Esto debería actualizar el RecyclerView cuando cambien los datos
        });
        Button buttonAddProduct = findViewById(R.id.button_add_product);
        buttonAddProduct.setOnClickListener(view -> showAddProductDialog());

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onUpdateClick(Product product) {
                showUpdateProductDialog(product);
            }

            @Override
            public void onDeleteClick(Product product) {
                productViewModel.delete(product);
                Toast.makeText(MainActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        EditText editTextPrice = dialogView.findViewById(R.id.edit_text_price);
        EditText editTextQuantity = dialogView.findViewById(R.id.edit_text_quantity);

        builder.setTitle("")
                .setPositiveButton("AGREGAR", (dialog, which) -> {
                    String name = editTextName.getText().toString();
                    String priceText = editTextPrice.getText().toString();
                    String quantityText = editTextQuantity.getText().toString();

                    if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                        Toast.makeText(MainActivity.this, "LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double price = Double.parseDouble(priceText);
                    int quantity = Integer.parseInt(quantityText);

                    Product product = new Product(name, price, quantity);
                    productViewModel.insert(product);

                    Toast.makeText(MainActivity.this, "PRODUCTO AGREGADO", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("CANCELAR", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showUpdateProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        EditText editTextPrice = dialogView.findViewById(R.id.edit_text_price);
        EditText editTextQuantity = dialogView.findViewById(R.id.edit_text_quantity);

        editTextName.setText(product.getName());
        editTextPrice.setText(String.valueOf(product.getPrice()));
        editTextQuantity.setText(String.valueOf(product.getQuantity()));

        builder.setTitle("")
                .setPositiveButton("UPDATE", (dialog, which) -> {
                    String name = editTextName.getText().toString();
                    String priceText = editTextPrice.getText().toString();
                    String quantityText = editTextQuantity.getText().toString();

                    if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                        Toast.makeText(MainActivity.this, "LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double price = Double.parseDouble(priceText);
                    int quantity = Integer.parseInt(quantityText);

                    product.setName(name);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    productViewModel.update(product);
                    Toast.makeText(MainActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("CANCELAR", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
