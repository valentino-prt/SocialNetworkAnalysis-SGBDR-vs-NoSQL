package org.example.socialnetworkanalysisnosql.services;


import org.example.socialnetworkanalysisnosql.data.User;
import org.example.socialnetworkanalysisnosql.repos.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service("noSqlUserService")
public class UserService {

    @Autowired
    private UserRepo repo;

    public UserService() {
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

    public int getBoughtProductCountCircle(String user, String product) {
        return repo.getBoughtProductCountCircle(user, product);
    }

    public void followUsersFromCsv() {
        repo.followUsersFromCsv();
    }

    public void buyProductsFromCsv() {
        repo.buyProductsFromCsv();
    }

}