package org.example.socialnetworkanalysisnosql.repos.product;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepoImpl implements CustomProductRepo {

    @Autowired
    private Driver driver;

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

}
