package com.productService.controller;

import com.productService.model.Product;
import com.productService.model.User;
import com.productService.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("product")
public class ProductController<T> {

   private ProductService service;

   @Autowired
    public ProductController(ProductService service){
        this.service = service;
    }


    @GetMapping("getAll")
    public ResponseEntity<Object>  getUsers(){
        return service.getProducts();
    }
    @GetMapping("get/{id}")
    public ResponseEntity<T> getUser(@PathVariable Long id){
        return service.getProduct(id);
    }

    @PostMapping("add")
    public ResponseEntity<Object> createUser(@Valid @RequestBody Product product){
        return service.addProduct(product);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,@RequestBody Product product){
        return service.updateProduct(id,product);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id){
        return service.deleteProduct(id);
    }

}
