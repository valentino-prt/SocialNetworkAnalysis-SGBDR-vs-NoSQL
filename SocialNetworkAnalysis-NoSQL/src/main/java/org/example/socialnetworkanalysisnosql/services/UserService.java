package org.example.socialnetworkanalysisnosql.services;


import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.example.socialnetworkanalysisnosql.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    public UserService() {
    }

    public User createUser(String s) {
        User user = new User(s);
        repo.save(user);
        return user;
    }

    public void buyProduct(User user, Product product) {
        user.buy(product);
        repo.save(user);
    }

    public void dump() {
        while (repo.count() > 0)
            repo.dumpLimit();
    }

    public void save(User user) {
        repo.save(user);
    }

    public void saveAll(List<User> users) {
        repo.saveAll(users);
    }

    public void insertRandomUsers(int count) {
        repo.insertRandomUsers(count);
    }

    public void insertRandomProducts(int count) {
        repo.insertRandomProducts(count);
    }

    public List<Product> getBoughtProductsCircle(User user) {
        return repo.getBoughtProductsCircle(user.getId());
    }

    public int getBoughtProductCountCircle(User user, Product product) {
        return repo.getBoughtProductCountCircle(user.getId(), product.getId());
    }

    public int getBoughtProductByProductAndDepth(String product, int depth) {
        return repo.findBoughtProductByProductAndDepth(product, depth);
    }

    public void followRandomUsers() {
        repo.followRandomUsers(repo.findAll());
    }
}
