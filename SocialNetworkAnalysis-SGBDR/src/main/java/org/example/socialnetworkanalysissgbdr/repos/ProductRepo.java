package org.example.socialnetworkanalysissgbdr.repos;

import org.infres14.neo4jtest.sql.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
