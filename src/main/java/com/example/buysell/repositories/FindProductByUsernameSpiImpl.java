package com.example.buysell.repositories;

import com.example.buysell.models.Product;

import com.example.buysell.services.CustomProductMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class FindProductByUsernameSpiImpl implements FindProductByUsernameSpi {
    private final NamedParameterJdbcTemplate jdbc;

    public FindProductByUsernameSpiImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Optional<Product>> findProductByUsername(String username) {
        String sql = "SELECT products.id, products.city, products.description, products.price, products.title, products.preview_image_id, products.date_of_created FROM users INNER JOIN products ON users.id = products.user_id WHERE users.email = :email";
        SqlParameterSource parameterSource = new MapSqlParameterSource("email", username);
        List<Optional<Product>> products = jdbc.query(sql, parameterSource, new CustomProductMapper());

        return products;
    }
}
