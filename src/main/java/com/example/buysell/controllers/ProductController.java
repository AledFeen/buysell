package com.example.buysell.controllers;

import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.FindProductByUsernameSpi;
import com.example.buysell.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        return "products";
    }

    @GetMapping("/personal")
    public String personalProducts(Model model, Principal principal) {
        model.addAttribute("products", productService.listProductsByUsername(principal.getName()));
        return "personal-products";
    }

    @GetMapping("/login")
    public String login(){ return "login"; }

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());

        return "product-info";
    }

    @GetMapping("/personal-product/{id}")
    public String personalProductInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        //User productCreator = productService.getProductCreator(product.getUser().getId());
        //model.addAttribute("user", productCreator.getEmail());
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("user", product.getUser().getEmail());
        model.addAttribute("currentUser", principal.getName());

        return "personal-product-info";
    }

    @PostMapping ("/personal-product/update/{id}")
    public String updateProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product updateProduct, @PathVariable Long id) throws SQLException, IOException {
        productService.updateProduct(id, updateProduct, file1, file2, file3);
        return "redirect:/personal";
    }

    @GetMapping("/create")
    public String handleCreateProduct() {
        return "create-product";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product, Principal principal) throws IOException {
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
