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
//        userService.dump();
//        productService.dump();
//
//        userService.insertRandomUsers(1000000);
//        productService.insertRandomProducts(10000);
//        userService.followUsersFromCsv();
//        userService.buyProductsFromCsv();
//
//        List<Product> products;
//        products = productService.getBoughtProductsCircle("User2", 2);
//        int count = userService.getBoughtProductCountCircle("User2", "Product3");
//        int count2 = productService.getBoughtProductByProductAndDepth("Product3", 2);
//        System.out.println("Products bought by User2 and his followers: ");
    }

    private void generateProducts(int count) {
        for (int i = 0; i < count; i++) {
            Product product = new Product("Product " + i, "Description " + i, rand.nextDouble() * 100);
            productService.save(product);
        }
    }

    private void createUsers(int count) {
        List<User> usersToInsert = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User("User " + i);
            usersToInsert.add(user);
            if (usersToInsert.size() == 1000) {
                userService.saveAll(usersToInsert);
                usersToInsert.clear();
            }
        }
        userService.saveAll(usersToInsert);
    }

    private void generateUsers(int count) {
        List<Product> products = productService.findAll();
        List<User> users = new ArrayList<>();
        List<User> usersToSave = new ArrayList<>();

        // Créer les utilisateurs
        for (int i = 0; i < count; i++) {
            User user = new User("User " + i);
            users.add(user);
        }

        for(int i = 0; i < count; i++) {
            // Acheter des produits aléatoires
            int finalI = i;
            IntStream.range(0, rand.nextInt(5)).forEach(k -> users.get(finalI).buy(products.get(rand.nextInt(products.size()))));

            // Suivre des utilisateurs aléatoires
            IntStream.range(0, rand.nextInt(10)).forEach(j -> {
                User userToFollow = users.get(rand.nextInt(users.size()));
                // Éviter que l'utilisateur se suive lui-même
                if (!users.get(finalI).equals(userToFollow)) {
                    if(userToFollow.getFollowerCount() < 10) {
                        users.get(finalI).follow(userToFollow);
                        userToFollow.followedBy(users.get(finalI));
                    }
                }
            });

            usersToSave.add(users.get(i));
            if (usersToSave.size() == 100000) {
                userService.saveAll(usersToSave);
                usersToSave.clear();
            }

        }

        userService.saveAll(usersToSave);

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

        userE.follow(userD);
        userE.buy(productA);

        // Sauvegarder tous les utilisateurs et produits
        userService.save(userA);
        userService.save(userB);
        userService.save(userC);
        userService.save(userD);
        userService.save(userE);
        userService.save(userF);

        List<Product> products = this.productService.getBoughtProductsCircle(userC.getName(), 1);
        int count = this.userService.getBoughtProductCountCircle(userA.getName(), productA.getName());
        int count2 = this.productService.getBoughtProductByProductAndDepth(productA.getName(), 2);

        System.out.println("Products bought by userA and his followers: ");
        products.forEach(System.out::println);

    }
}
