package org.example.socialnetworkanalysisnosql.services;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.example.socialnetworkanalysisnosql.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noSqlProductService")
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public ProductService() {
    }

    public Product createProduct(String s, String description, double price) {
        Product product = new Product(s, description, price);
        repo.save(product);
        return product;
    }

    public void dump() {
        while (repo.count() > 0)
            repo.dumpLimit();
    }

    public Product getProductByName(String s) {
        return this.repo.findByName(s);
    }

    public void save(Product product) {
        this.repo.save(product);
    }

    public List<Product> findAll() {
        return this.repo.findAll();
    }

}
