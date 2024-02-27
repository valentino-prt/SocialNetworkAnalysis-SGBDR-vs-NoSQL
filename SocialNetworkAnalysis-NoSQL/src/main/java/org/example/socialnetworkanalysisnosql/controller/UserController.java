package org.example.socialnetworkanalysisnosql.controller;

import org.example.socialnetworkanalysisnosql.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social")
public class UserController {

    private final UserService userService; // Assurez-vous d'injecter UserService

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("/followRandomUsers")
    public ResponseEntity<?> followRandomUsers() {
        try {
            userService.followRandomUsers();
            return ResponseEntity.ok().body("Les utilisateurs suivent maintenant des utilisateurs aléatoires.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors du suivi des utilisateurs : " + e.getMessage());
        }
    }
}
