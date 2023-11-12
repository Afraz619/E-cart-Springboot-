package com.example.springecart.dto;

import com.example.springecart.entity.Category;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ProductDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;


    private int categoryId;

    private double price;
    private double weight;
    private String description;
    private String imageName;
}
