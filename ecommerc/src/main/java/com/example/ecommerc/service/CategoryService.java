package com.example.ecommerc.service;

import com.example.ecommerc.dto.CategoryDto;
import com.example.ecommerc.dto.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService {
    public ResponseEntity<Result> getAllCategory();
    public ResponseEntity<Result> getCategoryByPage(int page, int size);
    public ResponseEntity<Result> addCategory(CategoryDto categoryDto, MultipartFile file) throws IOException;
    public ResponseEntity<Result> updateCategory(CategoryDto category, MultipartFile file, Long categoryId) throws IOException;
    public ResponseEntity<Result> deleteCategory(Long categoryId);
    public ResponseEntity<Result> activeCategory(Long categoryId);
    public ResponseEntity<Result> getCategoryById(Long categoryId);
    public ResponseEntity<Result> searchCategory(String keyword, int page, int size);
}
