package org.example.socialnetworkanalysisnosql.repos.user;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends Neo4jRepository<User, Long>, CustomUserRepository {

    @Query("MATCH (user:User) WITH user LIMIT 100000 DETACH DELETE user")
    void dumpLimit();

}
