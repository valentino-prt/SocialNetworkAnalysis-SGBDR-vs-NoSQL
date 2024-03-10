package org.example.socialnetworkanalysissgbdr.repos.product;

import org.example.socialnetworkanalysissgbdr.data.Product;

import java.util.List;

public interface CustomProductRepo {

    void insertRandomProducts(int count);

    int findBoughtProductByProductAndDepth(String productName, int depth);

    List<Product> getBoughtProductsCircle(String user, int depth);


}
