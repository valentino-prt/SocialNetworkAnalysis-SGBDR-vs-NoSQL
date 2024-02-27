package org.example.socialnetworkanalysissgbdr.repos.user;


import org.example.socialnetworkanalysissgbdr.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long>, CustomUserRepository {
}
