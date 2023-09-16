package com.example.buysell.repositories;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;

import java.sql.SQLException;
import java.util.List;

public interface UpdateProductByIdSpi {
    void updateProductById(long id, Product updatedProduct, List<Image> images) throws SQLException;
}
