package org.example.socialnetworkanalysissgbdr.services;


import org.example.socialnetworkanalysissgbdr.data.Product;
import org.example.socialnetworkanalysissgbdr.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo repo;

    @Autowired
    public ProductService(ProductRepo repo) {
        this.repo = repo;
    }

    public Product createProduct(String name, String description, double price) {
        Product product = new Product(name, description, price);
        repo.save(product);
        return product;
    }

    public void dump() {
        this.repo.deleteAll();
    }

    public Product getProductByName(String name) {
        return this.repo.findByName(name);
    }

    public void save(Product product) {
        this.repo.save(product);
    }

    public List<Product> findAll() {
        return this.repo.findAll();
    }
}
