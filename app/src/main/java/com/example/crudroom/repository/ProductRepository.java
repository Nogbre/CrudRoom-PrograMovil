package com.example.crudroom.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.crudroom.dao.ProductDao;
import com.example.crudroom.database.ProductDatabase;
import com.example.crudroom.model.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {
    private final ProductDao productDao;
    private final LiveData<List<Product>> allProducts;
    private final ExecutorService executorService;

    public ProductRepository(Application application) {
        ProductDatabase database = ProductDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insert(Product product) {
        executorService.execute(() -> productDao.insert(product));
    }

    public void update(Product product) {
        executorService.execute(() -> productDao.update(product));
    }

    public void delete(Product product) {
        executorService.execute(() -> productDao.delete(product));
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }
}
