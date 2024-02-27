package org.example.socialnetworkanalysisnosql.repos;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class UserRepoImpl implements CustomUserRepository {

    @Autowired
    private Driver driver;

    @Override
    public int findBoughtProductByProductAndDepth(String productName, int depth) {
        String cypherQuery = String.format(
                "MATCH (product:Product {name: \"%s\"})<-[:BOUGHT]-(buyer:User)<-[:FOLLOWERS*1..%d]-(follower:User)-[:BOUGHT]->(product)\n" +
                        "RETURN COUNT(DISTINCT follower)", productName, depth);

        try (Session session = driver.session()) {
            Result result = session.run(cypherQuery);
            if (result.hasNext()) {
                return result.next().get("COUNT(DISTINCT follower)").asInt();
            }
        }
        return 0;
    }

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
    public void insertRandomProducts(int count) {
        String cypherQuery = "UNWIND range(1, $count) AS id\n" +
                "CREATE (p:Product {name: 'Product ' + id, description: 'Description ' + id, price: rand() * 100})";

        try (Session session = driver.session()) {
            session.run(cypherQuery, Values.parameters("count", count));
        }

        String queryIndex = "CREATE INDEX product_id_index IF NOT EXISTS FOR (p:Product) ON (p.id)\n";
        try (Session session = driver.session()) {
            session.run(queryIndex);
        }
    }

    @Override
    public void followRandomUsers(List<User> users) {

        Random rand = new Random();
        int randomInt;
        int randomCountFollowers;
        List<Map<String, Object>> followPairs = new ArrayList<>();

        for(User user : users) {

            System.out.println("User : " + user.getId());

            for(int i = 0; i < rand.nextInt(0, 10); i++){
                do {
                    randomInt = rand.nextInt(0, users.size() - 1);
                } while (users.get(randomInt).getId() == user.getId());

                Map<String, Object> pair = new HashMap<>();
                pair.put("userName", user.getName());
                pair.put("followerUserName", users.get(randomInt).getName());
                followPairs.add(pair);

            }


        }

        try (Session session = driver.session()){
            session.executeWrite(tx -> {
                for (Map<String, Object> pair : followPairs) {

                    System.out.println("User : " + pair.get("userName") + " follow : " + pair.get("followerUserName"));

                    String cypherQuery = "MATCH (user:User), (followUser:User)\n" +
                            "WHERE user.name = $userName AND followUser.name = $followerUserName AND user.name <> followUser.name\n" +
                            "MERGE (user)-[:FOLLOWS]->(followUser)\n";
                    tx.run(cypherQuery, Values.parameters("userName", pair.get("userName"), "followerUserName", pair.get("followerUserName")));



                }
                return null;
            });

        }

    }
}
