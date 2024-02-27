package org.example.socialnetworkanalysisnosql.controller;

import org.example.socialnetworkanalysisnosql.services.ProductService;
import org.example.socialnetworkanalysisnosql.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social")
public class UserController {

    private final UserService userService; // Assurez-vous d'injecter UserService
    private final ProductService productService;

    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/createMultipleUsers")
    public ResponseEntity<?> createMultipleUsers(@RequestParam("count") int count) {
        if (count <= 0) {
            return ResponseEntity.badRequest().body("Le nombre d'utilisateurs à créer doit être supérieur à 0.");
        }

        try {
            userService.insertRandomUsers(count);
            return ResponseEntity.ok().body(count + " utilisateurs ont été créés avec succès.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création des utilisateurs : " + e.getMessage());
        }
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

    @PostMapping("/followRandomUsers")
    public ResponseEntity<?> followRandomUsers() {
        try {
            userService.followUsersFromCsv();
            return ResponseEntity.ok().body("Les utilisateurs suivent maintenant des utilisateurs aléatoires.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors du suivi des utilisateurs : " + e.getMessage());
        }
    }
}
