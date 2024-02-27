package org.example.socialnetworkanalysisnosql.repos.product;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomProductRepo {

    void insertRandomProducts(int count);

    int findBoughtProductByProductAndDepth(String productName, int depth);
    List<Product> getBoughtProductsCircle(String user, int depth);

}
