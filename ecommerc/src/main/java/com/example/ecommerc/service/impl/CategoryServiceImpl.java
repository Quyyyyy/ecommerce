package com.example.ecommerc.service.impl;

import com.example.ecommerc.dto.CategoryDto;
import com.example.ecommerc.dto.Result;
import com.example.ecommerc.entity.Category;
import com.example.ecommerc.exception.ResourceNotFoundException;
import com.example.ecommerc.repository.CategoryRepository;
import com.example.ecommerc.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Result> getAllCategory() {
        List<Category> cates = categoryRepository.findAll();
        List<CategoryDto> cateDtos = cates.stream().map((cate)->modelMapper.map(cate,CategoryDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result("SUCCESS","OK", cateDtos));
    }

    @Override
    public ResponseEntity<Result> getCategoryByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        List<Category> cates = categoryRepository.findAll(pageRequest ).toList();
        List<CategoryDto> cateDtos = cates.stream().map((cate)->modelMapper.map(cate,CategoryDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result("SUCCESS","OK", cateDtos));
    }

    @Override
    public ResponseEntity<Result> addCategory(CategoryDto categoryDto, MultipartFile file) throws IOException {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        String uploadDir = "upload/category-images";
        if (!Files.exists(Paths.get(uploadDir))) {
            Files.createDirectories(Paths.get(uploadDir));
        }
        String fileName = file.getOriginalFilename();
        String[] name = fileName.split("\\.");
        fileName = category.getName() + "." + name[1];
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath);
        category.setImage(filePath.toString());
        Category saveCategory = categoryRepository.save(category);
        CategoryDto categoryDto1 = modelMapper.map(saveCategory,CategoryDto.class);

        return ResponseEntity.ok(new Result("SUCCESS","OK",categoryDto1));
    }

    @Override
    public ResponseEntity<Result> updateCategory(CategoryDto category, MultipartFile file, Long categoryId) throws IOException {
        Category category1 = categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","id",categoryId)
        );
        if(category.getDescription()!=null)
            category1.setDescription(category.getDescription());
        if(category.getName()!=null)
            category1.setName(category.getName());
        if(!file.isEmpty()){
            String imagePath = category1.getImage();
            if(imagePath != null && !imagePath.isEmpty()){
                Files.deleteIfExists(Paths.get(imagePath));
            }
            String uploadDir = "upload/category-images";
            String filename = file.getOriginalFilename();
            String[] name = filename.split("\\.");
            filename = category1.getName() + "." + name[1];
            Path filePath = Paths.get(uploadDir,filename);
            Files.copy(file.getInputStream(),filePath);

            category1.setImage(filePath.toString());
        }
        Category saveCate = categoryRepository.save(category1);
        CategoryDto categoryDto = modelMapper.map(saveCate,CategoryDto.class);

        return ResponseEntity.ok(new Result("SUCCESS","OK",categoryDto));
    }

    @Override
    public ResponseEntity<Result> deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","id",categoryId)
        );
        category.setStatus(false);
        Category saveCate = categoryRepository.save(category);
        CategoryDto categoryDto = modelMapper.map(saveCate,CategoryDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK", categoryDto));
    }

    @Override
    public ResponseEntity<Result> activeCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","id",categoryId)
        );
        category.setStatus(true);
        Category saveCate = categoryRepository.save(category);
        CategoryDto categoryDto = modelMapper.map(saveCate,CategoryDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK", categoryDto));
    }

    @Override
    public ResponseEntity<Result> getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","id",categoryId)
        );
        CategoryDto categoryDto = modelMapper.map(category,CategoryDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK", categoryDto));
    }

    @Override
    public ResponseEntity<Result> searchCategory(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Category> cates = categoryRepository.findCategoriesByKeyword(keyword, pageRequest).toList();
        List<CategoryDto> catedtos = cates.stream().map((cate)->modelMapper.map(cate,CategoryDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result("SUCCESS","OK", catedtos));
    }
}
