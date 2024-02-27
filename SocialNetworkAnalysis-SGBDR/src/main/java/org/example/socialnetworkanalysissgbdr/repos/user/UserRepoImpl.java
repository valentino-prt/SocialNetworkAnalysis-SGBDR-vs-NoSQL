package org.example.socialnetworkanalysissgbdr.repos.user;

import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepoImpl implements CustomUserRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void insertRandomUsers(int count) {
        final int batchSize = 1000; // Nombre d'insertions par lot

        String sql = "INSERT INTO users (name) VALUES (?)";

        for (int i = 0; i < count; i += batchSize) {
            final int start = i;
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    int userNameIndex = start + j + 1;
                    ps.setString(1, "User" + userNameIndex);
                }

                @Override
                public int getBatchSize() {
                    return Math.min(batchSize, count - start);
                }
            });
        }
    }


    @Override
    public void followUsersFromCsv() {
        // Supposons que le chemin du fichier CSV soit disponible et correct
        String csvFilePath = "chemin/vers/followPairs.csv";

        // Requête SQL pour insérer des relations de suivi à partir du CSV
        String sql = "INSERT INTO Follows (follower_id, followed_id) " +
                "SELECT f1.id AS follower_id, f2.id AS followed_id " +
                "FROM (SELECT id, name FROM Users) AS f1 " +
                "CROSS JOIN (SELECT id, name FROM Users) AS f2 " +
                "JOIN CSVREAD('" + csvFilePath + "', 'user,followedUser', 'header=true') AS csv " +
                "ON f1.name = csv.user AND f2.name = csv.followedUser " +
                "WHERE NOT EXISTS ( " +
                "  SELECT 1 FROM Follows WHERE follower_id = f1.id AND followed_id = f2.id " +
                ")";

        jdbcTemplate.execute(sql);
    }

    @Override
    public int getBoughtProductCountCircle(String userId, String productId) {
        String sql = "SELECT COUNT(DISTINCT ubp.user_id) " +
                "FROM user_bought_products ubp " +
                "JOIN user_followers uf ON ubp.user_id = uf.follower_id " +
                "WHERE uf.user_id = ? AND ubp.product_id = ?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        return jdbcTemplate.queryForObject(sql, new Object[]{userId, productId}, Integer.class);
    }


    @Override
    public void buyProductsFromCsv() {
        String csvFilePath = "/path/to/buyPairs.csv"; // Chemin d'accès au fichier CSV

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> records = csvReader.readAll();
            records.forEach(record -> {
                String userName = record[0];
                String productName = record[1];

                // Trouver les identifiants utilisateur et produit
                Long userId = jdbcTemplate.queryForObject(
                        "SELECT id FROM users WHERE name = ?",
                        new Object[]{userName},
                        Long.class);
                Long productId = jdbcTemplate.queryForObject(
                        "SELECT id FROM products WHERE name = ?",
                        new Object[]{productName},
                        Long.class);

                if (userId != null && productId != null) {
                    // Insérer la relation d'achat
                    jdbcTemplate.update(
                            "INSERT INTO user_bought_products (user_id, product_id) VALUES (?, ?)",
                            userId, productId);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }



}
