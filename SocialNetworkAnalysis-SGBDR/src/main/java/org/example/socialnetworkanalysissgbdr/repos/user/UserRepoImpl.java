package org.example.socialnetworkanalysissgbdr.repos.user;

import com.opencsv.CSVWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.socialnetworkanalysissgbdr.data.Product;
import org.example.socialnetworkanalysissgbdr.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Driver;
import java.util.*;

@Repository
public class UserRepoImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void insertRandomUsers(int count) {
        for (int i = 1; i <= count; i++) {
            User user = new User();
            user.setName("User" + i);
            entityManager.persist(user);
            if (i % 1000 == 0) { // Flush et clear pour éviter OutOfMemory
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    @Override
    public void followRandomUsers(List<User> users) {

    }

    @Override
    public List<Product> getBoughtProductsCircle(long userId) {
        return null;
    }

//    @Override
//    public void followRandomUsers(List<User> users) {
//
//        Random rand = new Random();
//        int randomInt;
//        int randomCountFollowers;
//        List<Map<String, String>> followPairs = new ArrayList<>();
//
//
//        for(User user : users) {
//
//            System.out.println("User : " + user.getId());
//
//            for(int i = 0; i < rand.nextInt(0, 10); i++){
//                do {
//                    randomInt = rand.nextInt(0, users.size() - 1);
//                } while (users.get(randomInt).getId() == user.getId());
//
//                Map<String, String> pair = new HashMap<>();
//                pair.put("userName", user.getName());
//                pair.put("followerUserName", users.get(randomInt).getName());
//                followPairs.add(pair);
//
//            }
//        }
//
//        File file = new File("neo4j/import/followPairs.csv");
//        try {
//            FileWriter outputFile = new FileWriter(file);
//            CSVWriter writer = new CSVWriter(outputFile);
//
//            String[] header = {"user", "followedUser"};
//            writer.writeNext(header);
//
//            for (Map<String, String> pair : followPairs) {
//                String[] data = {pair.get("userName"), pair.get("followerUserName")};
//                writer.writeNext(data);
//            }
//
//            writer.close();
//
//            try (Session session = driver.session()){
//                session.run("LOAD CSV WITH HEADERS FROM 'file:///followPairs.csv' AS row\n" +
//                        "MATCH (user:User {name: row.user}), (followUser:User {name: row.followedUser})\n" +
//                        "MERGE (user)-[:FOLLOWS]->(followUser)\n");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }



//        List<Map<String, String>> userToInsert = new ArrayList<>();
//        for (Map<String, String> pair : followPairs) {
//            userToInsert.add(pair);
//
//            if (userToInsert.size() == 1000) {
//                try (Session session = driver.session()){
//
//                    session.executeWrite(tx -> {
//                        for (Map<String, String> p : userToInsert) {
//
//                            System.out.println("User : " + p.get("userName") + " follow : " + p.get("followerUserName"));
//
//                            String cypherQuery = "MATCH (user:User), (followUser:User)\n" +
//                                    "WHERE user.name = $userName AND followUser.name = $followerUserName AND user.name <> followUser.name\n" +
//                                    "MERGE (user)-[:FOLLOWS]->(followUser)\n";
//                            tx.run(cypherQuery, Values.parameters("userName", p.get("userName"), "followerUserName", p.get("followerUserName")));
//
//                        }
//                        return null;
//                    });
//                }
//                userToInsert.clear();
//            }
//
//        }



//    }
//
//    @Override
//    public List<Product> getBoughtProductsCircle(long userId) {
//        return null;
//    }
}
