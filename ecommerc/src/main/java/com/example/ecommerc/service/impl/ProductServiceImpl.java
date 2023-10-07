package com.example.ecommerc.service.impl;

import com.example.ecommerc.dto.CategoryDto;
import com.example.ecommerc.dto.ProductDto;
import com.example.ecommerc.dto.ProductImageDto;
import com.example.ecommerc.dto.Result;
import com.example.ecommerc.entity.Category;
import com.example.ecommerc.entity.Product;
import com.example.ecommerc.entity.ProductImage;
import com.example.ecommerc.exception.APIException;
import com.example.ecommerc.exception.ResourceNotFoundException;
import com.example.ecommerc.repository.CategoryRepository;
import com.example.ecommerc.repository.ProductImageRepository;
import com.example.ecommerc.repository.ProductRepository;
import com.example.ecommerc.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;
    private ProductImageRepository productImageRepository;

    @Override
    public ResponseEntity<Result> getAllProduct(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageRequest);
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product pro:products){
            ProductDto productDto = modelMapper.map(pro,ProductDto.class);
            productDto.setCategoryDto(modelMapper.map(pro.getCategory(),CategoryDto.class));
            productDtos.add(productDto);
        }
        return ResponseEntity.ok(new Result("SUCCESS", "OK", productDtos));
    }

    @Override
    public ResponseEntity<Result> getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()->new APIException("NOT_FOUND_PRODUCT")
        );
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setCategoryDto(modelMapper.map(product.getCategory(), CategoryDto.class));
        List<ProductImage> productImageList = productImageRepository.findProductImageByProduct(product);
        List<ProductImageDto> productImageDtos = productImageList.stream().map((productImage -> modelMapper.map(productImage, ProductImageDto.class))).collect(Collectors.toList());
        productDto.setImageDtos(productImageDtos);
        return ResponseEntity.ok(new Result("SUCCESS","OK",productDto));
    }

    @Override
    public ResponseEntity<Result> addProduct(ProductDto productDto, List<MultipartFile> files) throws IOException {
        if(productRepository.existsProductByTitle(productDto.getTitle())){
            throw new IOException("EXIST PRODUCT!");
        }else{
            Product product = new Product();
            product.setTitle(productDto.getTitle());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setQuantity(productDto.getQuantity());
            Category category = categoryRepository.findById(productDto.getCategoryDto().getId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Category","id",productDto.getCategoryDto().getId())
            );
            product.setCategory(category);
            Product savePro = productRepository.save(product);

            String uploadDir = "upload/product_image";
            if(!Files.exists(Paths.get(uploadDir))){
                Files.createDirectories(Paths.get(uploadDir));
            }
            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file:files){
                String fileName = file.getOriginalFilename();
                String[] name = fileName.split("\\.");
                String randomID = UUID.randomUUID().toString();
                fileName = product.getTitle() + randomID + "." + name[1];
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath);

                ProductImage productImage = new ProductImage();
                productImage.setSourceImage(filePath.toString());
                productImage.setProduct(savePro);
                productImages.add(productImage);
            }
            productImageRepository.saveAll(productImages);
            //Product savePro1 = productRepository.save(product);
            ProductDto productDto1 = modelMapper.map(savePro,ProductDto.class);
            productDto1.setCategoryDto(productDto.getCategoryDto());
            List<ProductImageDto> productImageDtos = productImages.stream()
                    .map((productImage -> modelMapper.map(productImage, ProductImageDto.class))).collect(Collectors.toList());
            productDto1.setImageDtos(productImageDtos);
            return ResponseEntity.ok(new Result("SUCCESS","OK",productDto1));
        }
    }

    @Override
    public ResponseEntity<Result> updateProduct(ProductDto productDto, List<MultipartFile> files, Long productId) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","id",productId)
        );
        if(productDto.getTitle()!=null)
            product.setTitle(productDto.getTitle());
        if(productDto.getPrice()!=null)
            product.setPrice(productDto.getPrice());
        if(productDto.getDescription()!=null)
            product.setDescription(productDto.getDescription());
        if(productDto.getQuantity()!=null)
            product.setQuantity(productDto.getQuantity());
        if(productDto.getCategoryDto()!=null){
            Category category = categoryRepository.findById(productDto.getCategoryDto().getId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Category","id",productDto.getCategoryDto().getId())
            );
            product.setCategory(category);
        }

        String uploadDir = "upload/product_image";
        if(files.size() != 0){
            List<ProductImage> productImageList =  productImageRepository.findProductImageByProduct(product);
            for(ProductImage productImage:productImageList){
                String imagePath = productImage.getSourceImage();
                if(imagePath!=null && !imagePath.isEmpty()){
                    Files.deleteIfExists(Paths.get(imagePath));
                }
            }
            productImageRepository.deleteAll(productImageList);

            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file:files){
                String fileName = file.getOriginalFilename();
                String[] name = fileName.split("\\.");
                String randomID = UUID.randomUUID().toString();
                fileName = product.getTitle() + randomID + "." + name[1];
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath);

                ProductImage productImage = new ProductImage();
                productImage.setSourceImage(filePath.toString());
                productImage.setProduct(product);
                productImages.add(productImage);
            }
            //product.setProductImages(productImages);
            productImageRepository.saveAll(productImages);
        }
        Product savePro1 = productRepository.save(product);
        List<ProductImage> imageSavePro = productImageRepository.findProductImageByProduct(savePro1);
        ProductDto productDto1 = modelMapper.map(savePro1,ProductDto.class);
        productDto1.setCategoryDto(productDto.getCategoryDto());
        List<ProductImageDto> productImageDtos = imageSavePro.stream()
                .map((productImage -> modelMapper.map(productImage, ProductImageDto.class))).collect(Collectors.toList());
        productDto1.setImageDtos(productImageDtos);
        return ResponseEntity.ok(new Result("SUCCESS","OK",productDto1));
    }

    @Override
    public ResponseEntity<Result> deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","id",productId)
        );
        product.setStatus(false);
        Product savePro = productRepository.save(product);
        ProductDto productDto = modelMapper.map(savePro, ProductDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK",productDto));
    }

    @Override
    public ResponseEntity<Result> activeProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","id",productId)
        );
        product.setStatus(true);
        Product savePro = productRepository.save(product);
        ProductDto productDto = modelMapper.map(savePro, ProductDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK",productDto));
    }

    @Override
    public ResponseEntity<Result> searchProduct(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> products = productRepository.findProductsByKeyword(keyword, pageRequest);
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product pro:products){
            ProductDto productDto = modelMapper.map(pro,ProductDto.class);
            productDto.setCategoryDto(modelMapper.map(pro.getCategory(),CategoryDto.class));
            List<ProductImage> productImageList = productImageRepository.findProductImageByProduct(pro);
            List<ProductImageDto> images = productImageList.stream().
                    map((ima)->modelMapper.map(ima,ProductImageDto.class)).collect(Collectors.toList());
            productDto.setImageDtos(images);
            productDtos.add(productDto);
        }
        return  ResponseEntity.ok(new Result("SUCCESS", "OK", productDtos));
    }

    @Override
    public ResponseEntity<Result> searchProductByCategory(Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> products = productRepository.findProductByCategoryId(categoryId, pageRequest);
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product pro:products){
            ProductDto productDto = modelMapper.map(pro,ProductDto.class);
            productDto.setCategoryDto(modelMapper.map(pro.getCategory(),CategoryDto.class));
            List<ProductImage> productImageList = productImageRepository.findProductImageByProduct(pro);
            List<ProductImageDto> images = productImageList.stream().
                    map((ima)->modelMapper.map(ima,ProductImageDto.class)).collect(Collectors.toList());
            productDto.setImageDtos(images);
            productDtos.add(productDto);
        }
        return  ResponseEntity.ok(new Result("SUCCESS", "OK", productDtos));
    }

    @Override
    public ResponseEntity<Result> getBestSales() {
        return null;
    }
}
