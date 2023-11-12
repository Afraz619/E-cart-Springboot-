package com.example.springecart.service;

import com.example.springecart.entity.Category;
import com.example.springecart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    //save category
    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    //list category
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    //delete
    public void removeCategoryById(int id){
         categoryRepository.deleteById(id);
    }

    public Optional<Category> getCategoryById(int id){
        return categoryRepository.findById(id);
    }


}
