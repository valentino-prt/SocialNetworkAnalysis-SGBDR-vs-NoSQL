package org.example.socialnetworkanalysisnosql.repos;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends Neo4jRepository<Product, Long> {
    Product findByName(String s);

    @Query("MATCH (n:Product) WITH n LIMIT 100000 DETACH DELETE n")
    void dumpLimit();

}
