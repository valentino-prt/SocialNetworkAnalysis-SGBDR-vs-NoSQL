package org.example.socialnetworkanalysisnosql.repos.product;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends Neo4jRepository<Product, Long>, CustomProductRepo {
    Product findByName(String s);

    @Query("MATCH (n:Product) WITH n LIMIT 100000 DETACH DELETE n")
    void dumpLimit();

}
