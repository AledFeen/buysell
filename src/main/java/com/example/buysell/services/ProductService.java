package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.FindProductByUsernameSpi;
import com.example.buysell.repositories.ProductRepository;
import com.example.buysell.repositories.UpdateProductByIdSpi;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    private final NamedParameterJdbcTemplate jdbc;
    @Autowired
    FindProductByUsernameSpi findProductByUsernameSpi;
    @Autowired
    UpdateProductByIdSpi updateProductByIdSpi;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public List<Product> listProductsByUsername(String username) {

        List<Optional<Product>> products = findProductByUsernameSpi.findProductByUsername(username);
        List<Product> result = products.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        return result;
    }

    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {}; Author: {}", product.getTitle(), product.getUser().getEmail());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public void updateProduct(long id, Product updatedProduct, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException, SQLException {
        Image image1;
        Image image2;
        Image image3;
        List<Image> images = new ArrayList<>();
        Random random = new Random();
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            image1.setId(random.nextLong());
            images.add(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            images.add(image2);
            image2.setId(random.nextLong());
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            images.add(image3);
            image3.setId(random.nextLong());
        }
        updateProductByIdSpi.updateProductById(id, updatedProduct, images);
    }

    private User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


}
