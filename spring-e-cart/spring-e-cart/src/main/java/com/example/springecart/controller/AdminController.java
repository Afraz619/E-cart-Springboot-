package com.example.springecart.controller;

import com.example.springecart.dto.ProductDTO;
import com.example.springecart.entity.Category;
import com.example.springecart.entity.Product;
import com.example.springecart.service.CategoryService;
import com.example.springecart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }

    //after clicking the categories button && it should display categories list also
    @GetMapping("/admin/categories")
    public String getCat(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());      //it will get a list in the categories.html which run in loop :53
        return "categories";
    }

    //after clicking add categories button
    @GetMapping("/admin/categories/add")                                                    //need to send a category object
    public String getCatAdd(Model model){
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }

    //after clicking add categories button it need an category object posting the object through this method
    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") Category category){
       categoryService.addCategory(category);
        return "redirect:/admin/categories";

    }

    //delete button :(?Here we will not use DeleteMapping (used for RestApi))Only @GetMapping and @PostMapping used when we are working on the view
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable int id){
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    // categories.html :43 it will see that if the category is already present with the given {id} then it will update "or" else it will create a new
    //update and Add ??
    @GetMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id,Model model){
        Optional<Category> category = categoryService.getCategoryById(id);
        if(category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        }
        else{
            return "404";
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //Product Section

    @GetMapping("/admin/products")
    public String getProduct(Model model){
        model.addAttribute("products",productService.getAllProduct());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String productAddGet(Model model){
        model.addAttribute("productDTO",new ProductDTO());                                  //Here we are sending ProductDto object
        model.addAttribute("categories",categoryService.getAllCategory());                  //For selecting category : 56
        return "productsAdd";
    }

    //after clicking submit button
    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName")String imgName) throws IOException{

        //we are getting productDTO object we have to insert into normal ProductObject
        Product product=new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());

        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());

        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());

        //?????
        String imageUUID;
        if(!file.isEmpty()){
            imageUUID=file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath,file.getBytes());
        }
        else {
            imageUUID =imgName;
        }
        product.setImageName(imageUUID);
        //?????

        productService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id,Model model){
        Product product =productService.getProductById(id).get();
        ProductDTO productDTO=new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());  //???
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO",productDTO);

        return "productsAdd";
    }
}
