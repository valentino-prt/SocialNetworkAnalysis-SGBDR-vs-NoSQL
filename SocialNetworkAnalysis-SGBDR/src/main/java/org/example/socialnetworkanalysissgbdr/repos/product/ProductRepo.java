package org.example.socialnetworkanalysissgbdr.repos.product;

import org.example.socialnetworkanalysissgbdr.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, CustomProductRepo {
//    Product findByName(String s);
//
//    List<Product> getBoughtProductsCircle(@Param("userId") long userId);

}
