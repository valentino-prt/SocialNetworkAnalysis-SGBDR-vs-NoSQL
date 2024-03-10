package org.example.socialnetworkanalysissgbdr.services;

import org.example.socialnetworkanalysissgbdr.data.Product;
import org.example.socialnetworkanalysissgbdr.data.User;
import org.example.socialnetworkanalysissgbdr.repos.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    public UserService() {
    }

    public void dump() {
        while (repo.count() > 0)
            repo.deleteAllInBatch();
    }

    public void save(User user) {
        repo.save(user);
    }

    public void saveAll(List<User> users) {
        repo.saveAll(users);
    }

    public void insertRandomUsers(int count) { repo.insertRandomUsers(count);
    }

    public void followUsersFromCsv() {
        repo.followUsersFromCsv();
    }

    public void buyProductsFromCsv() {
        repo.buyProductsFromCsv();
    }

    public int getBoughtProductCountCircle(String userName, String productName) {
        return repo.getBoughtProductCountCircle(userName, productName);

    }


//
//    public UserService() {
//    }
//
//    public void dump() {
//        repo.deleteAllInBatch();
//    }
//
//    public void save(User user) {
//        repo.save(user);
//    }
//
//    public void saveAll(List<User> users) {
//        repo.saveAll(users);
//    }
//
//    public void createMultipleUsers(int count) {
//        repo.insertRandomUsers(count);
//    }
}
