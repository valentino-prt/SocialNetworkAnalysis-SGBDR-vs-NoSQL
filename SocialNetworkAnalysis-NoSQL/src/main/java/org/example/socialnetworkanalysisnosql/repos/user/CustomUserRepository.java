package org.example.socialnetworkanalysisnosql.repos.user;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomUserRepository {

    void insertRandomUsers(int count);

    void followRandomUsers(List<User> users);

    List<Product> getBoughtProductsCircle(@Param("userId") long userId);

}
