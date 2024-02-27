package org.example.socialnetworkanalysisnosql.repos.product;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

        String queryIndex = "CREATE INDEX product_name_index IF NOT EXISTS FOR (p:Product) ON (p.name)\n";
        try (Session session = driver.session()) {
            session.run(queryIndex);
        }
    }

    @Override
    public List<Product> getBoughtProductsCircle(String user, int depth) {
        String query = String.format("MATCH (influencer:User) <-[:FOLLOWERS*1..%d]-(follower:User)-[:BOUGHT]->(product:Product)\n"+
                "WHERE influencer.name = $name\n" +
                "WITH product, COLLECT(DISTINCT follower) AS buyers\n" +
                "RETURN id(product) AS id, product.name AS name, product.description AS description, product.price AS price, buyers AS boughtBy", depth);

        try (Session session = driver.session()) {
            Result result = session.run(query, Values.parameters("name", user));
            return result.list(record -> new Product(
                    record.get("id").asLong(),
                    record.get("name").asString(),
                    record.get("description").asString(),
                    record.get("price").asDouble()
            ));
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
