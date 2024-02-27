package org.example.socialnetworkanalysisnosql.repos.user;

import com.opencsv.CSVWriter;
import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Repository
public class UserRepoImpl implements CustomUserRepository {

    @Autowired
    private Driver driver;

    @Override
    public void insertRandomUsers(int count) {
        String cypherQuery = "UNWIND range(1, $count) AS id\n" +
                "CREATE (u:User {name: 'User' + id})";

        try (Session session = driver.session()) {
            session.run(cypherQuery, Values.parameters("count", count));
        }

        String queryIndex = "CREATE INDEX user_name_index IF NOT EXISTS FOR (u:User) ON (u.name)\n";
        try (Session session = driver.session()) {
            session.run(queryIndex);
        }
    }

    @Override
    public int getBoughtProductCountCircle(long userId, long productId) {

        String query = "MATCH (influencer:User) <-[:FOLLOWERS*1..]-(follower:User)-[:BOUGHT]->(product:Product)\n" +
                "WHERE id(influencer) = $userId AND id(product) = $productId\n" +
                "WITH COUNT(DISTINCT follower) AS buyersCount\n" +
                "RETURN buyersCount";

        try (Session session = driver.session()) {
            Result result = session.run(query, Values.parameters("userId", userId, "productId", productId));
            if (result.hasNext()) {
                return result.next().get("buyersCount").asInt();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public void buyRandomProducts(List<Product> products, int userCount) {

        Random rand = new Random();
        int randomInt;
        int randomCountFollowers;
        List<Map<String, String>> productPairs = new ArrayList<>();


        for(int i = 1; i <= userCount; i++) {

            System.out.println("User buy : " + i);

            for(int j = 0; j < rand.nextInt(0, 10); j++){
                randomInt = rand.nextInt(0, products.size() - 1);
                Map<String, String> pair = new HashMap<>();
                pair.put("userName", "User" + i);
                pair.put("productName", products.get(randomInt).getName());
                productPairs.add(pair);
            }
        }

        File file = new File("neo4j/import/buyPairs.csv");
        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            String[] header = {"user", "product"};
            writer.writeNext(header);

            for (Map<String, String> pair : productPairs) {
                String[] data = {pair.get("userName"), pair.get("productName")};
                writer.writeNext(data);
            }

            writer.close();

            try (Session session = driver.session()){
                session.run("LOAD CSV WITH HEADERS FROM 'file:///buyPairs.csv' AS row\n" +
                        "MATCH (user:User {name: row.user}), (product:Product {name: row.product})\n" +
                        "MERGE (user)-[:BOUGHT]->(product)\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void followRandomUsers(List<User> users) {

        Random rand = new Random();
        int randomInt;
        int randomCountFollowers;
        List<Map<String, String>> followPairs = new ArrayList<>();


        for(User user : users) {

            System.out.println("User : " + user.getId());

            for(int i = 0; i < rand.nextInt(0, 10); i++){
                do {
                    randomInt = rand.nextInt(0, users.size() - 1);
                } while (users.get(randomInt).getId() == user.getId());

                Map<String, String> pair = new HashMap<>();
                pair.put("userName", user.getName());
                pair.put("followerUserName", users.get(randomInt).getName());
                followPairs.add(pair);

            }
        }

        File file = new File("neo4j/import/followPairs.csv");
        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            String[] header = {"user", "followedUser"};
            writer.writeNext(header);

            for (Map<String, String> pair : followPairs) {
                String[] data = {pair.get("userName"), pair.get("followerUserName")};
                writer.writeNext(data);
            }

            writer.close();

            try (Session session = driver.session()){
                session.run("LOAD CSV WITH HEADERS FROM 'file:///followPairs.csv' AS row\n" +
                        "MATCH (user:User {name: row.user}), (followedUser:User {name: row.followedUser})\n" +
                        "MERGE (user)-[:FOLLOWERS]->(followedUser)\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



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



    }

    @Override
    public List<Product> getBoughtProductsCircle(long userId) {
        return null;
    }
}
