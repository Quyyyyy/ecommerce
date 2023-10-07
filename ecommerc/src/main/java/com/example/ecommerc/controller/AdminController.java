package com.example.ecommerc.controller;

import java.io.IOException;
import java.util.List;

import com.example.ecommerc.dto.CategoryDto;
import com.example.ecommerc.dto.CouponDto;
import com.example.ecommerc.dto.ProductDto;
import com.example.ecommerc.dto.Result;
import com.example.ecommerc.security.JwtService;
import com.example.ecommerc.service.CategoryService;
import com.example.ecommerc.service.CouponService;
import com.example.ecommerc.service.OrderService;
import com.example.ecommerc.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private CategoryService categoryService;
    private ProductService productService;
    private CouponService couponService;
    private JwtService jwtService;
    private OrderService orderService;

    @GetMapping("/category/all")
    public ResponseEntity<Result> getAllCategoryByPage(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "8") int size) {
        return categoryService.getCategoryByPage(page, size);
    }

    @PostMapping("/category/add")
    public ResponseEntity<Result> addCategory(@RequestPart("categoryDto") CategoryDto categoryDto,
                                              @RequestPart("file") MultipartFile file) throws IOException {
        return categoryService.addCategory(categoryDto,file);
    }

    @PostMapping("/category/update/{categoryId}")
    public ResponseEntity<Result> updateCategory(@RequestPart("category") CategoryDto category,
                                                 @RequestPart("image") MultipartFile file,
                                                 @PathVariable("categoryId") Long categoryId) throws IOException {
        return categoryService.updateCategory(category,file,categoryId);
    }

    @PostMapping("/category/delete/{categoryId}")
    public ResponseEntity<Result> deleteCategory(@PathVariable("categoryId") Long categoryId){
        return categoryService.deleteCategory(categoryId);
    }

    @PostMapping("/category/active/{categoryId}")
    public ResponseEntity<Result> activeCategory(@PathVariable("categoryId") Long categoryId){
        return categoryService.activeCategory(categoryId);
    }

    @GetMapping("/category/detail/{categoryId}")
    public ResponseEntity<Result> getCategoryById(@PathVariable("categoryId") Long categoryId){
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping("/category/search")
    public ResponseEntity<Result> findCategoriesByKeyword(@RequestParam String name,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "8") int size){
        return categoryService.searchCategory(name, page,size);
    }




    @PostMapping("/product/add")
    public ResponseEntity<Result> addProduct(@RequestPart("product") ProductDto product,
                                             @RequestPart("files") List<MultipartFile> files) throws IOException {

        return productService.addProduct(product, files);
    }

    @PostMapping("/product/update/{productId}")
    public ResponseEntity<Result> updateProduct(@RequestPart("product") ProductDto product,
                                                @RequestPart("files") List<MultipartFile> files,
                                                @PathVariable("productId") Long productId) throws IOException {
        return productService.updateProduct(product,files, productId);
    }

    @GetMapping("/product/detail/{productId}")
    public ResponseEntity<Result> getProductById(@PathVariable("productId") Long productId){
        return productService.getProductById(productId);
    }

    @GetMapping("/product/all")
    public ResponseEntity<Result> getAllProduct(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "8") int size){
        return productService.getAllProduct(page, size);
    }

    @PostMapping("/product/delete/{productId}")
    public ResponseEntity<Result> deleteProduct(@PathVariable("productId") Long productId){
        return productService.deleteProduct(productId);
    }

    @PostMapping("/product/active/{productId}")
    public ResponseEntity<Result> activeProduct(@PathVariable("productId") Long productId){
        return productService.activeProduct(productId);
    }

    @GetMapping("/product/find-by-category/{categoryId}")
    public ResponseEntity<Result> findProductByCategory(@PathVariable("categoryId") Long categoryId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "8") int size){
        return productService.searchProductByCategory(categoryId, page, size);
    }

    @GetMapping("/product/search")
    public ResponseEntity<Result> findProductsByKeyword(@RequestParam String title,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "8") int size){
        return productService.searchProduct(title, page, size);
    }





    @GetMapping("/coupon/all")
    public ResponseEntity<Result> getAllCoupon(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "8") int size){
        return couponService.getAllCoupon(page, size);
    }

    @PostMapping("/coupon/add")
    public ResponseEntity<Result> addCoupon(@RequestBody CouponDto couponDto) {
        return couponService.addCoupon(couponDto);
    }

    @PostMapping("/coupon/update/{couponId}")
    public ResponseEntity<Result> updateCoupon(@RequestBody CouponDto couponDto,
                                               @PathVariable("couponId") Long couponId){
        return couponService.updateCoupon(couponDto,couponId);
    }

    @PostMapping("/coupon/delete/{couponId}")
    public ResponseEntity<Result> deleteCoupon(@PathVariable("couponId") Long couponId){
        return couponService.deleteCoupon(couponId);
    }

    @PostMapping("/coupon/active/{couponId}")
    public ResponseEntity<Result> activeCoupon(@PathVariable("couponId") Long couponId){
        return couponService.activeCoupon(couponId);
    }


    @GetMapping("/order/all")
    public ResponseEntity<Result> getAllOrder(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "8") int size){
        return orderService.getAllOrder(page, size);
    }
}
