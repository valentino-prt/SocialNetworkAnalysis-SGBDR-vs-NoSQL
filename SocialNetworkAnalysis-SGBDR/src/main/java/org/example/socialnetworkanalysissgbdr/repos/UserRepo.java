package org.example.socialnetworkanalysissgbdr.repos;

import org.infres14.neo4jtest.sql.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
