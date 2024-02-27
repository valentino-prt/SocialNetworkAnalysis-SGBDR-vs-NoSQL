package org.example.socialnetworkanalysisnosql.services;


import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.example.socialnetworkanalysisnosql.repos.product.ProductRepo;
import org.example.socialnetworkanalysisnosql.repos.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noSqlUserService")
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserService() {
    }

    public void dump() {
        while (userRepo.count() > 0)
            userRepo.dumpLimit();
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public void saveAll(List<User> users) {
        userRepo.saveAll(users);
    }

    public void insertRandomUsers(int count) {
        userRepo.insertRandomUsers(count);
    }

    public int getBoughtProductCountCircle(User user, Product product) {
        return userRepo.getBoughtProductCountCircle(user.getId(), product.getId());
    }

    public void followRandomUsers() {
        userRepo.followRandomUsers(userRepo.findAll());
    }
}
