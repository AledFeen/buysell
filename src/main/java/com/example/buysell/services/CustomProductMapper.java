package com.example.buysell.services;

import com.example.buysell.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
@RequiredArgsConstructor
public class CustomProductMapper implements RowMapper<Optional<Product>> {
    @Override
    public Optional<Product> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setTitle(rs.getString("title"));
        product.setPrice(rs.getInt("price"));
        product.setCity(rs.getString("city"));
        product.setDescription(rs.getString("description"));
        product.setPreviewImageId(rs.getLong("preview_image_id"));
        product.setDateOfCreated(rs.getTimestamp("date_of_created").toLocalDateTime());

        System.out.println("Get products " + product.getTitle() + " " + product.getId() + " " + product.getPrice());
        return Optional.of(product);
    }
}
