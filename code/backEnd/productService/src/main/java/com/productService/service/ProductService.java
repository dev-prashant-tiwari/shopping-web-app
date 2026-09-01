package com.productService.service;

import com.productService.constants.ApplicationConstants;
import com.productService.model.Product;
import com.productService.model.User;
import com.productService.repository.ProductRepository;
import com.productService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@org.springframework.stereotype.Service
public class ProductService<T> {

    ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository){
     this.productRepository = productRepository;
    }
    @Autowired
    PasswordEncoder passwordEncoder;
    public ResponseEntity<Object> addProduct(Product product){
        try {
            log.info("adding the product {}",product.toString());
            Product result = productRepository.save(product);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG+" "+ex.getMessage());
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getProducts(){
        try{
            List<Product> products = productRepository.findAll();
            return new ResponseEntity<>(products,HttpStatus.OK);
        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG+" "+ex.getMessage());
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateProduct(Long id, Product updatedProduct) {
        try{
            log.info("updating user with id {} and user: {}",id,updatedProduct);
            Optional<Product> existingProductObj = productRepository.findById(id);
            if(existingProductObj.isPresent()){
               Product existingProduct = existingProductObj.get();
               Product result = updateNonNullFields(existingProduct,updatedProduct);
               return new ResponseEntity<>(result,HttpStatus.OK);
            }else{
                log.warn("used does not exist with provided id");
                return new ResponseEntity<>("user could not be updated",HttpStatus.NOT_FOUND);
            }

        }catch (Exception ex){
            log.error("exception occurred while updating the user with id {}",id);
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG+ex,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Product updateNonNullFields(Product existingProduct, Product newProduct) {
        if(Objects.nonNull(newProduct.getName())){
            existingProduct.setName(newProduct.getName());
        }
        if(Objects.nonNull(newProduct.getDescription())){
            existingProduct.setDescription(newProduct.getDescription());
        }
        if(Objects.nonNull(newProduct.getPrice())){
            existingProduct.setPrice(newProduct.getPrice());
        }
        if(Objects.nonNull(newProduct.getStockQuantity())){
            existingProduct.setStockQuantity(newProduct.getStockQuantity());
        }
        return productRepository.save(existingProduct);
    }

    public ResponseEntity<T> getProduct(Long id) {
        try {
            log.info("getting user with id {}",id);
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()){
                return new ResponseEntity<T>((T) product.get(),HttpStatus.OK);
            }
            else{
                log.warn("user does not exist with id {}",id);
                return new ResponseEntity<T>((T) "user does not exist",HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG, HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<T>((T)(ApplicationConstants.GENEXCEPTIONMSG+ex),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteProduct(Long id) {
        try {
            log.info("deleting the user with id {}",id);
            productRepository.deleteById(id);
            log.warn("user deleted successfully with id {}",id);
            return new ResponseEntity<>("Product deleted with id "+id+" successfully",HttpStatus.OK);

        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG, HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG+ex,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
