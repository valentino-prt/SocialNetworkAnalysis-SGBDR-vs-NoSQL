package org.example.socialnetworkanalysissgbdr.repos.user;

import org.example.socialnetworkanalysissgbdr.data.Product;
import org.example.socialnetworkanalysissgbdr.data.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomUserRepository {

    void insertRandomUsers(int count);
    void followUsersFromCsv();
    int getBoughtProductCountCircle(String userId, String productId);
    void buyProductsFromCsv();

}
