package org.example.socialnetworkanalysisnosql.repos.user;

public interface CustomUserRepository {

    void insertRandomUsers(int count);
    void followUsersFromCsv();
    int getBoughtProductCountCircle(String userId, String productId);
    void buyProductsFromCsv();
}
