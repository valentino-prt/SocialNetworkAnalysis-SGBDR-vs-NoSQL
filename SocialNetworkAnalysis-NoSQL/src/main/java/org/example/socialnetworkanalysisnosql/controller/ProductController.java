package org.example.socialnetworkanalysisnosql.controller;

import org.example.socialnetworkanalysisnosql.data.Product;
import org.example.socialnetworkanalysisnosql.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final org.example.socialnetworkanalysisnosql.services.ProductService noSqlProductService = new ProductService();


    // Création d'un produit
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        // Logique pour créer un produit
        return ResponseEntity.ok().body("ok");
    }

    // Assigner un achat de produit à un utilisateur
    @PostMapping("/{productId}/buy/{userId}")
    public ResponseEntity<?> assignProductToUser(@PathVariable Long productId, @PathVariable Long userId) {
        // Logique pour assigner un produit à un utilisateur
        return ResponseEntity.ok().body("ok");
    }

    // Obtenir la liste et le nombre des produits commandés par les cercles de followers d’un individu
    @GetMapping("/influence/{userId}")
    public ResponseEntity<?> getProductInfluence(@PathVariable Long userId) {
        // Logique pour obtenir les produits et leur influence
        return ResponseEntity.ok().body("ok");
    }

    // Obtenir le nombre de personnes ayant commandé un produit dans un cercle de followers
    @GetMapping("/{productId}/popularity")
    public ResponseEntity<?> getProductPopularity(@PathVariable Long productId) {
        // Logique pour obtenir la popularité d'un produit
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/createSingleProduct")
    public ResponseEntity<?> createSingleProduct(@RequestBody Product product) {
        try {
            noSqlProductService.createProduct(product.getName(), product.getDescription(), product.getPrice());
            return ResponseEntity.ok().body("Le produit a été créé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création du produit : " + e.getMessage());
        }
    }

    @PostMapping("/createMultipleProducts")
    public ResponseEntity<?> createMultipleProducts(@RequestParam("count") int count) {
        if (count <= 0) {
            return ResponseEntity.badRequest().body("Le nombre de produits à créer doit être supérieur à 0.");
        }

        try {
            userService.insertRandomProducts(count);
            return ResponseEntity.ok().body(count + " produits ont été créés avec succès.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création des produits : " + e.getMessage());
        }
    }
}
