package com.example.ecommerc.service;

import com.example.ecommerc.dto.ProductDto;
import com.example.ecommerc.dto.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    public ResponseEntity<Result> getAllProduct(int page, int size);
    public ResponseEntity<Result> getProductById(Long productId);
    public ResponseEntity<Result> addProduct(ProductDto productDto, List<MultipartFile> files) throws IOException;
    public ResponseEntity<Result> updateProduct(ProductDto productDto, List<MultipartFile> files, Long productId) throws IOException;
    public ResponseEntity<Result> deleteProduct(Long productId);
    public ResponseEntity<Result> activeProduct(Long productId);
    public ResponseEntity<Result> searchProduct(String keyword, int page, int size);
    public ResponseEntity<Result> searchProductByCategory(Long categoryId, int page, int size);
    public ResponseEntity<Result> getBestSales();

}
