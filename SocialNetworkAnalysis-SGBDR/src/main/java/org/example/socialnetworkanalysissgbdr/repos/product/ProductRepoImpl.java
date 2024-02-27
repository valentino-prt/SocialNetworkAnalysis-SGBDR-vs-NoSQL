package org.example.socialnetworkanalysissgbdr.repos.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class ProductRepoImpl implements CustomProductRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void insertRandomProducts(int count) {
        String sqlInsertProduct = "INSERT INTO product(name, description, price) VALUES (?, ?, ?)";

        // Préparez une liste de tableaux d'objets représentant les paramètres de chaque ligne à insérer
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            batchArgs.add(new Object[]{"Product " + i, "Description " + i, Math.random() * 100});
        }

        // Utilisez batchUpdate pour insérer en masse
        jdbcTemplate.batchUpdate(sqlInsertProduct, batchArgs);

        // Assurez-vous que l'index sur product_id existe déjà ou créez-le ici si nécessaire
        String sqlCreateIndex = "CREATE INDEX IF NOT EXISTS product_id_index ON product(id);";
        jdbcTemplate.execute(sqlCreateIndex);
    }


    @Override
    public int findBoughtProductByProductAndDepth(String productName, int depth) {
        return 0;
    }

//    @Override
//    public int findBoughtProductByProductAndDepth(String productName, int depth) {
//        String cypherQuery = String.format(
//                "MATCH (product:Product {name: \"%s\"})<-[:BOUGHT]-(buyer:User)<-[:FOLLOWERS*1..%d]-(follower:User)-[:BOUGHT]->(product)\n" +
//                        "RETURN COUNT(DISTINCT follower)", productName, depth);
//
//        try (Session session = driver.session()) {
//            Result result = session.run(cypherQuery);
//            if (result.hasNext()) {
//                return result.next().get("COUNT(DISTINCT follower)").asInt();
//            }
//        }
//        return 0;
//    }

}
