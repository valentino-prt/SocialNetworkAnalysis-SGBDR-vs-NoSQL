package org.example.socialnetworkanalysisnosql.repos;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends Neo4jRepository<Product, Long> {
    Product findByName(String s);

}
