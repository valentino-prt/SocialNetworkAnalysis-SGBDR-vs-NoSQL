package org.example.socialnetworkanalysissgbdr.repos.product;

import org.example.socialnetworkanalysissgbdr.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
        String sql = "WITH RECURSIVE followers_graph AS (" +
                "  SELECT f.follower_id, f.user_id, 1 AS depth" +
                "  FROM user_followers f" +
                "  WHERE f.user_id IN (SELECT id FROM users WHERE name = ?)" +
                "  UNION ALL" +
                "  SELECT f.follower_id, fg.user_id, fg.depth + 1" +
                "  FROM user_followers f" +
                "  JOIN followers_graph fg ON f.user_id = fg.follower_id" +
                "  WHERE fg.depth < ?" +
                "), bought_products AS (" +
                "  SELECT DISTINCT p.id" +
                "  FROM products p" +
                "  WHERE p.name = ?" +
                "), followers_who_bought AS (" +
                "  SELECT ubp.user_id" +
                "  FROM user_bought_products ubp" +
                "  JOIN bought_products bp ON ubp.product_id = bp.id" +
                "  JOIN followers_graph fg ON ubp.user_id = fg.follower_id" +
                ")" +
                "SELECT COUNT(DISTINCT user_id) FROM followers_who_bought;";

        return jdbcTemplate.queryForObject(sql, new Object[]{productName, depth, productName}, Integer.class);
    }
    public List<Product> getBoughtProductsCircle(String userName, int depth) {
        // Supposons qu'une stratégie simplifiée est utilisée pour limiter la profondeur
        // Cette requête est un exemple simplifié pour illustrer une approche possible.
        String sql = "WITH recursive followers_graph AS (" +
                "SELECT user_id, follower_id, 1 AS depth FROM user_followers uf " +
                "JOIN users u ON uf.user_id = u.id WHERE u.name = ? " +
                "UNION ALL " +
                "SELECT uf.user_id, uf.follower_id, fg.depth + 1 FROM user_followers uf " +
                "JOIN followers_graph fg ON uf.user_id = fg.follower_id WHERE fg.depth < ?" +
                ") " +
                "SELECT DISTINCT p.* FROM products p " +
                "JOIN user_bought_products ubp ON p.id = ubp.product_id " +
                "JOIN followers_graph fg ON ubp.user_id = fg.follower_id " +
                "ORDER BY p.id;";

        return jdbcTemplate.query(sql, new Object[]{userName, depth}, new ProductRowMapper());
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getDouble("price"));
            return product;
        }
    }

}
