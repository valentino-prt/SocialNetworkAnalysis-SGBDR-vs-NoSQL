package org.example.socialnetworkanalysissgbdr.repos.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.socialnetworkanalysissgbdr.data.Product;
import org.example.socialnetworkanalysissgbdr.data.User;
import org.springframework.stereotype.Repository;

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
    public int getBoughtProductCountCircle(String userId, String productId) {
        String query = "WITH RECURSIVE follower_tree AS (" +
                "SELECT follower_id, user_id, 1 AS level " +
                "FROM user_followers " +
                "WHERE user_id = :userId " + // Utilisez l'ID de l'utilisateur passé à la méthode
                "UNION ALL " +
                "SELECT uf.follower_id, ft.user_id, ft.level + 1 " +
                "FROM user_followers uf " +
                "INNER JOIN follower_tree ft ON uf.user_id = ft.follower_id " +
                "WHERE ft.level < 4 " + // Limitez à 4 niveaux de followers
                ") " +
                "SELECT COUNT(*) " +
                "FROM products p " +
                "INNER JOIN user_bought_products ubp ON p.id = ubp.product_id " +
                "INNER JOIN follower_tree ft ON ubp.user_id = ft.follower_id " +
                "WHERE p.id = :productId"; // Utilisez l'ID du produit passé à la méthode

        // Exécutez la requête avec les paramètres
        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter("userId", Long.parseLong(userId))
                .setParameter("productId", Long.parseLong(productId));
        Number result = (Number) nativeQuery.getSingleResult();
        return result.intValue();
    }

    @Override
    @Transactional
    public void followUsersFromCsv() {
        // Création d'une table temporaire pour stocker les paires d'utilisateurs suivis
        entityManager.createNativeQuery("CREATE TEMP TABLE temp_follow_pairs (user_name varchar(255), followed_user_name varchar(255));").executeUpdate();

        // Chargement des données depuis le CSV dans la table temporaire
        entityManager.createNativeQuery("COPY temp_follow_pairs(user_name, followed_user_name) FROM '/csv/followPairs.csv' DELIMITER ',' CSV HEADER;").executeUpdate();

        // Insertion des relations de suivi dans la table 'user_followers'
        entityManager.createNativeQuery("INSERT INTO user_followers(user_id, follower_id) " +
                        "SELECT u.id, f.id FROM temp_follow_pairs " +
                        "JOIN users u ON u.name = temp_follow_pairs.user_name " +
                        "JOIN users f ON f.name = temp_follow_pairs.followed_user_name " +
                        "WHERE NOT EXISTS ( " +
                        "SELECT 1 FROM user_followers uf " +
                        "WHERE uf.user_id = u.id AND uf.follower_id = f.id);")
                .executeUpdate();

        // Suppression de la table temporaire
        entityManager.createNativeQuery("DROP TABLE temp_follow_pairs;").executeUpdate();
    }


    @Override
    @Transactional
    public void buyProductsFromCsv() {
        // Étape 1: Création d'une table temporaire
        entityManager.createNativeQuery(
                "CREATE TEMP TABLE IF NOT EXISTS temp_buy_pairs (" +
                        "user_name varchar(255), " +
                        "product_name varchar(255))"
        ).executeUpdate();

        // Étape 2: Chargement des données du fichier CSV dans la table temporaire
        // Remplacer '/chemin/vers/le/fichier/buyPairs.csv' par le chemin réel du fichier sur le serveur
        entityManager.createNativeQuery(
                "COPY temp_buy_pairs(user_name, product_name) " +
                        "FROM '/csv/buyPairs.csv' DELIMITER ',' CSV HEADER"
        ).executeUpdate();

        // Étape 3: Insertion des données dans la table 'user_bought_products'
        entityManager.createNativeQuery(
                "INSERT INTO user_bought_products(user_id, product_id) " +
                        "SELECT u.id, p.id " +
                        "FROM temp_buy_pairs " +
                        "JOIN users u ON u.name = temp_buy_pairs.user_name " +
                        "JOIN products p ON p.name = temp_buy_pairs.product_name"
        ).executeUpdate();

        // Étape 4: Suppression de la table temporaire
        entityManager.createNativeQuery(
                "DROP TABLE temp_buy_pairs"
        ).executeUpdate();

    }
}
