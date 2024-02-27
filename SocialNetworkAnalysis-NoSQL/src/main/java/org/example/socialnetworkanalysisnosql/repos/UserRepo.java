package org.example.socialnetworkanalysisnosql.repos;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends Neo4jRepository<User, Long>, CustomUserRepository {

    @Query("MATCH (influencer:User) <-[:FOLLOWERS*1..]-(follower:User)-[:BOUGHT]->(product:Product)\n" +
            "WHERE id(influencer) = $userId\n" +
            "WITH product, COLLECT(DISTINCT follower) AS buyers\n" +
            "RETURN product.id AS id, product.name AS name, product.description AS description, product.price AS price, buyers AS boughtBy")
    List<Product> getBoughtProductsCircle(@Param("userId") long userId);

    @Query("MATCH (influencer:User) <-[:FOLLOWERS*1..]-(follower:User)-[:BOUGHT]->(product:Product)\n" +
            "WHERE id(influencer) = $userId AND id(product) = $productId\n" +
            "WITH COUNT(DISTINCT follower) AS buyersCount\n" +
            "RETURN buyersCount")
    int getBoughtProductCountCircle(@Param("userId") long userId, @Param("productId") long productId);

    @Query("MATCH (user:User) WITH user LIMIT 100000 DETACH DELETE user")
    void dumpLimit();


}
