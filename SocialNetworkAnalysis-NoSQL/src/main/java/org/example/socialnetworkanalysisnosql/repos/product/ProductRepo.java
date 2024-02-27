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

    @Query("MATCH (influencer:User) <-[:FOLLOWERS*1..]-(follower:User)-[:BOUGHT]->(product:Product)\n" +
            "WHERE id(influencer) = $userId\n" +
            "WITH product, COLLECT(DISTINCT follower) AS buyers\n" +
            "RETURN product.id AS id, product.name AS name, product.description AS description, product.price AS price, buyers AS boughtBy")
    List<Product> getBoughtProductsCircle(@Param("userId") long userId);

    @Query("MATCH (n:Product) WITH n LIMIT 100000 DETACH DELETE n")
    void dumpLimit();

}
