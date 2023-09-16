package com.example.buysell.repositories;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class UpdateProductByIdSpiImpl implements UpdateProductByIdSpi {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    @Transactional
    public void updateProductById(long id, Product updatedProduct, List<Image> images) throws SQLException {
        String sql = "UPDATE products SET city = :city, description = :description, price = :price, title = :title WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("city", updatedProduct.getCity());
        parameters.addValue("description", updatedProduct.getDescription());
        parameters.addValue("price", updatedProduct.getPrice());
        parameters.addValue("title", updatedProduct.getTitle());
        parameters.addValue("id", id);
        jdbc.update(sql, parameters);

        if(!images.isEmpty()) {

            sql = "DELETE from images where product_id = :id";
            parameters = new MapSqlParameterSource();
            parameters.addValue("id", id);
            jdbc.update(sql, parameters);

            sql = "Insert into images(id, bytes, content_type, is_preview_image, name, original_file_name, size, product_id) VALUES (:id, :bytes, :content_type, :is_preview_image, :name, :original_file_name, :size, :product_id)";
            for (var im : images ) {
                SerialBlob serialBlob = new SerialBlob(im.getBytes());
                parameters = new MapSqlParameterSource();
                parameters.addValue("id", im.getId());
                parameters.addValue("bytes", serialBlob);
                parameters.addValue("content_type", im.getContentType());
                parameters.addValue("is_preview_image", im.isPreviewImage());
                parameters.addValue("name", im.getName());
                parameters.addValue("original_file_name", im.getOriginalFileName());
                parameters.addValue("size", im.getSize());
                parameters.addValue("product_id", id);

                jdbc.update(sql, parameters);
            }
        }
    }
}
