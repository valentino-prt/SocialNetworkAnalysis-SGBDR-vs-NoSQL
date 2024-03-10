package org.example.socialnetworkanalysisnosql.controller;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.services.ProductService;
import org.example.socialnetworkanalysisnosql.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social")
public class Controller {

    private final UserService userService; // Assurez-vous d'injecter UserService
    private final ProductService productService;

    public Controller(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/importFromCsv")
    public ResponseEntity<?> importFromCsv() {
        try {

            userService.dump();
            productService.dump();

            long startTime, endTime, durationUsers, durationProducts, durationFollow, durationBuy;

            System.out.println("Importing data from CSV");

            startTime = System.nanoTime();
            userService.insertRandomUsers(1000000);
            endTime = System.nanoTime();
            durationUsers = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

            System.out.println("Users created in " + durationUsers + " ms");

            startTime = System.nanoTime();
            productService.insertRandomProducts(10000);
            endTime = System.nanoTime();
            durationProducts = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

            System.out.println("Products created in " + durationProducts + " ms");

            startTime = System.nanoTime();
            userService.followUsersFromCsv();
            endTime = System.nanoTime();
            durationFollow = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

            System.out.println("Users followed in " + durationFollow + " ms");

            startTime = System.nanoTime();
            userService.buyProductsFromCsv();
            endTime = System.nanoTime();
            durationBuy = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

            System.out.println("Products bought in " + durationBuy + " ms");

            return ResponseEntity.ok().body("Base importée avec succès." +
                    "\nDurée de création des utilisateurs : " + durationUsers + " ms" +
                    "\nDurée de création des produits : " + durationProducts + " ms" +
                    "\nDurée de suivi des utilisateurs : " + durationFollow + " ms" +
                    "\nDurée d'achat de produits : " + durationBuy + " ms");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'achat de produits à partir du fichier CSV : " + e.getMessage());
        }
    }

    @GetMapping("/getBoughtProductCircle/{userName}/{depth}")
    public ResponseEntity<?> getBoughtProductCircle(@PathVariable String userName, @PathVariable int depth) {
        try {
            long startTime, endTime, duration;

            startTime = System.nanoTime();
            List<Product> products = productService.getBoughtProductsCircle(userName, depth);
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1_000_000; // Convertir en millisecondes
            StringBuilder sb = new StringBuilder();
            for (Product product : products) {
                sb.append(product.getName()).append("\n");
            }
            return ResponseEntity.ok().body("Produits achetés par " + userName + " et ses followers : " + sb + "\nDurée : " + duration + " ms");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération des produits achetés par " + userName + " et ses followers : " + e.getMessage());
        }
    }

    @GetMapping("/getBoughtProductCountCircle/{userName}/{productName}")
    public ResponseEntity<?> getBoughtProductCountCircle(@PathVariable String userName, @PathVariable String productName) {
        try {
            long startTime, endTime, duration;

            startTime = System.nanoTime();
            int count = userService.getBoughtProductCountCircle(userName, productName);

            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

            return ResponseEntity.ok().body("Nombre de produits achetés par " + userName + " et ses followers : " + count + "\nDurée : " + duration + " ms");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération du nombre de produits achetés par " + userName + " et ses followers : " + e.getMessage());
        }
    }

    @GetMapping("/findBoughtProductByProductAndDepth/{productName}/{depth}")
    public ResponseEntity<?> findBoughtProductByProductAndDepth(@PathVariable String productName, @PathVariable int depth) {
        try {
            long startTime, endTime, duration;

            startTime = System.nanoTime();
            int count = productService.getBoughtProductByProductAndDepth(productName, depth);
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

            return ResponseEntity.ok().body("Nombre de produits achetés par " + productName + " et ses followers : " + count + "\nDurée : " + duration + " ms");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération du nombre de produits achetés par " + productName + " et ses followers : " + e.getMessage());
        }
    }
}
