package com.example.buysell.repositories;

import com.example.buysell.models.Product;

import java.util.List;
import java.util.Optional;

public interface FindProductByUsernameSpi {
    List<Optional<Product>> findProductByUsername(String username);
}
