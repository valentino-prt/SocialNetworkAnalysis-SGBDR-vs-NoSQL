package org.example.socialnetworkanalysisnosql.repos.user;

import com.opencsv.CSVWriter;
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
    public void buyProductsFromCsv() {

    try (Session session = driver.session()){
        session.run("LOAD CSV WITH HEADERS FROM 'file:///buyPairs.csv' AS row\n" +
                "CALL{WITH row MATCH (user:User {name: row.user}), (product:Product {name: row.product})\n" +
                "MERGE (user)-[:BOUGHT]->(product)} IN TRANSACTIONS\n");
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    }

    @Override
    public void followUsersFromCsv() {

    try (Session session = driver.session()){
        session.run("LOAD CSV WITH HEADERS FROM 'file:///followPairs.csv' AS row\n" +
                "CALL {WITH row MATCH (user:User {name: row.user}), (followedUser:User {name: row.followedUser})\n" +
                "MERGE (user)-[:FOLLOWERS]->(followedUser)} IN TRANSACTIONS\n");
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    }

    @Override
    public int getBoughtProductCountCircle(String userName, String productName) {

        String query = "MATCH (influencer:User) <-[:FOLLOWERS*1..4]-(follower:User)-[:BOUGHT]->(product:Product)\n" +
                "WHERE influencer.name = $userName AND product.name = $productName\n" +
                "WITH COUNT(DISTINCT follower) AS buyersCount\n" +
                "RETURN buyersCount";

        try (Session session = driver.session()) {
            Result result = session.run(query, Values.parameters("userName", userName, "productName", productName));
            if (result.hasNext()) {
                return result.next().get("buyersCount").asInt();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

}
