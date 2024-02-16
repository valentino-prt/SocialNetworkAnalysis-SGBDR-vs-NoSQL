package org.example.socialnetworkanalysisnosql;

import lombok.AllArgsConstructor;
import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.data.User;
import org.example.socialnetworkanalysisnosql.services.ProductService;
import org.example.socialnetworkanalysisnosql.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class StartRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    private final Random rand = new Random();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.dump();
        productService.dump();

        sampleDB();
    }

    private void generateProducts(int count) {
        for (int i = 0; i < count; i++) {
            Product product = new Product("Product " + i, "Description " + i, rand.nextDouble() * 100);
            productService.save(product);
        }
    }

    private void generateUsers(int count) {
        List<Product> products = productService.findAll();
        List<User> users = new ArrayList<>();

        // Créer les utilisateurs
        for (int i = 0; i < count; i++) {
            User user = new User("User " + i);
            users.add(user);
        }

        // Assigner les followers et les produits de façon aléatoire
        users.forEach(user -> {
            // Acheter des produits aléatoires
            IntStream.range(0, rand.nextInt(6)).forEach(k -> user.buy(products.get(rand.nextInt(products.size()))));

            // Suivre des utilisateurs aléatoires
            IntStream.range(0, rand.nextInt(21)).forEach(j -> {
                User userToFollow = users.get(rand.nextInt(users.size()));
                // Éviter que l'utilisateur se suive lui-même
                if (!user.equals(userToFollow)) {
                    if(userToFollow.getFollowerCount() < 20) {
                        user.follow(userToFollow);
                        userToFollow.followedBy(user);
                        }
                }
            });
        });

        userService.saveAll(users);
    }

    private void sampleDB() {
        // Produits précédemment définis
        Product productA = new Product("ProductA", "DescriptionA", 100.0);
        Product productB = new Product("ProductB", "DescriptionB", 150.0);
        // Nouveaux produits exclusifs
        Product productC = new Product("ProductC", "DescriptionC", 200.0);
        Product productD = new Product("ProductD", "DescriptionD", 250.0);
        productService.save(productA);
        productService.save(productB);
        productService.save(productC);
        productService.save(productD);

        // Utilisateurs et relations précédemment définis
        User userA = new User("UserA");
        User userB = new User("UserB");
        User userC = new User("UserC");
        User userD = new User("UserD");

        // Définir les relations d'achat pour les produits partagés
        userA.buy(productA);
        userA.buy(productB);
        userB.follow(userA);
        userC.follow(userB);
        userD.follow(userC);
        userB.buy(productA);
        userB.buy(productB);
        userC.buy(productA);
        userC.buy(productB);
        userD.buy(productA);
        userD.buy(productB);

        // Assigner des produits exclusifs à certains utilisateurs
        User userE = new User("UserE"); // Nouvel utilisateur qui achètera un produit exclusif
        userE.buy(productC); // UserE achète ProductC, qui n'est acheté par personne d'autre
        User userF = new User("UserF"); // Encore un nouvel utilisateur pour un autre produit exclusif
        userF.buy(productD); // UserF achète ProductD, exclusivement

        // Sauvegarder tous les utilisateurs et produits
        userService.save(userA);
        userService.save(userB);
        userService.save(userC);
        userService.save(userD);
        userService.save(userE);
        userService.save(userF);
    }
}
