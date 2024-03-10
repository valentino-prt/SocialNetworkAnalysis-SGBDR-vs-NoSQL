package org.example.socialnetworkanalysissgbdr.repos.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.socialnetworkanalysissgbdr.data.Product;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepoImpl implements CustomProductRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void insertRandomProducts(int count) {
        int batchSize = 1000;

        for (int i = 1; i <= count; i++) {
            String name = "Product" + i;
            String description = "Description " + i;
            double price = Math.random() * 100;

            String sql = "INSERT INTO products (name, description, price) VALUES (:name, :description, :price)";

            Query query = entityManager.createNativeQuery(sql)
                    .setParameter("name", name)
                    .setParameter("description", description)
                    .setParameter("price", price);

            query.executeUpdate();

            if (i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }



    @Override
    @Transactional
    public int findBoughtProductByProductAndDepth(String productName, int depth) {
        String query = "WITH RECURSIVE follower_tree AS (" +
                "    SELECT id AS user_id, 0 AS level " +
                "    FROM users " +
                "    WHERE id IN (SELECT user_id FROM user_bought_products WHERE product_id = (SELECT id FROM products WHERE name = :productName)) " +
                "    UNION ALL " +
                "    SELECT uf.follower_id, ft.level + 1 " +
                "    FROM user_followers uf " +
                "    INNER JOIN follower_tree ft ON uf.user_id = ft.user_id " +
                "    WHERE ft.level < :depth " +
                ") " +
                "SELECT COUNT(DISTINCT user_id) " +
                "FROM follower_tree WHERE level > 0;"; // Exclut le niveau 0, qui est l'utilisateur qui a fait le premier achat

        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter("productName", productName)
                .setParameter("depth", depth);
        Number result = (Number) nativeQuery.getSingleResult();
        return result.intValue();
    }

    @Override
    @Transactional
    public List<Product> getBoughtProductsCircle(String userName, int depth) {
        String sqlQuery = "WITH RECURSIVE follower_tree AS (" +
                "    SELECT user_id, follower_id, 1 AS level " +
                "    FROM user_followers " +
                "    WHERE follower_id = (SELECT id FROM users WHERE name = :userName) " +
                "    UNION ALL " +
                "    SELECT uf.user_id, uf.follower_id, ft.level + 1 " +
                "    FROM user_followers uf " +
                "    INNER JOIN follower_tree ft ON uf.follower_id = ft.user_id " +
                "    WHERE ft.level < :depth " +
                ") " +
                "SELECT p.id, p.name, p.description, p.price, ARRAY_AGG(DISTINCT ft.user_id) AS boughtBy " +
                "FROM products p " +
                "JOIN user_bought_products ubp ON p.id = ubp.product_id " +
                "JOIN follower_tree ft ON ubp.user_id = ft.user_id " +
                "GROUP BY p.id, p.name, p.description, p.price";

        List products = entityManager.createNativeQuery(sqlQuery)
                .setParameter("userName", userName)
                .setParameter("depth", depth)
                .unwrap(NativeQuery.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuple, String[] aliases) {
                        return new Product(
                                (String) tuple[1],
                                (String) tuple[2],
                                ((Number) tuple[3]).doubleValue()
                        );
                    }

                    @Override
                    public List transformList(List collection) {
                        return collection;
                    }
                })
                .getResultList();

        return products;
    }

}
