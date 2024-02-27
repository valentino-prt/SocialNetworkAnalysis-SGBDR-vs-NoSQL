package org.example.socialnetworkanalysisnosql.repos.product;

public interface CustomProductRepo {

    void insertRandomProducts(int count);

    int findBoughtProductByProductAndDepth(String productName, int depth);

}
