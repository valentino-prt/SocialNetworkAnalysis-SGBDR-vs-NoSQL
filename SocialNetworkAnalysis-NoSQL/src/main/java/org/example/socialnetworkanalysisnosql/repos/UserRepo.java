package org.example.socialnetworkanalysisnosql.repos;

import org.example.socialnetworkanalysisnosql.data.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends Neo4jRepository<User, Long> {
}
