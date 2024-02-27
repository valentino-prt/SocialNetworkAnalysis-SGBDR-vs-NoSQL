package org.example.socialnetworkanalysissgbdr.repos.user;


import org.example.socialnetworkanalysissgbdr.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long>, CustomUserRepository {
//
//    @Query("MATCH (influencer:User) <-[:FOLLOWERS*1..]-(follower:User)-[:BOUGHT]->(product:Product)\n" +
//            "WHERE id(influencer) = $userId AND id(product) = $productId\n" +
//            "WITH COUNT(DISTINCT follower) AS buyersCount\n" +
//            "RETURN buyersCount")
//    int getBoughtProductCountCircle(@Param("userId") long userId, @Param("productId") long productId);
//
//    @Query("MATCH (user:User) WITH user LIMIT 100000 DETACH DELETE user")
//    void dumpLimit();


}
