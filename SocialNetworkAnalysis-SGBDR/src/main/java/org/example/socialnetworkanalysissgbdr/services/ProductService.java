package org.example.socialnetworkanalysissgbdr.services;

import org.example.socialnetworkanalysissgbdr.data.Product;
import org.example.socialnetworkanalysissgbdr.data.User;
import org.example.socialnetworkanalysissgbdr.repos.product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SqlProductService")
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public ProductService() {
    }


    public void dump() {
        repo.deleteAllInBatch();
    }

    public void insertRandomProducts(int count) {
        repo.insertRandomProducts(count);
    }

//    public List<Product> getBoughtProductsCircle(User user) {
//        return repo.getBoughtProductsCircle(user.getId());
//    }

    public int getBoughtProductByProductAndDepth(String product, int depth) {
        return repo.findBoughtProductByProductAndDepth(product, depth);
    }

//    public Product getProductByName(String s) {
//        return this.repo.findByName(s);
//    }


    public List<Product> findAll() {
        return this.repo.findAll();
    }



}
