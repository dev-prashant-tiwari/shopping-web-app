package com.orderService.controller;

import com.orderService.model.Category;
import com.orderService.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController<T> {
    @Autowired
    CategoryRepository categoryRepository;
    @PostMapping("add")
    public ResponseEntity<T> addCategory(@RequestBody Category category){
        Category cat = categoryRepository.save(category);
        return new ResponseEntity<>((T)cat, HttpStatus.CREATED);
    }
}
