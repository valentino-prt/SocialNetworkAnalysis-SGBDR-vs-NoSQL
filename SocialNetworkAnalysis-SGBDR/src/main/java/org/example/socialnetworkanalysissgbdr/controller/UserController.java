package org.example.socialnetworkanalysissgbdr.controller;
import org.example.socialnetworkanalysissgbdr.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createMultiple")
    public ResponseEntity<?> createMultipleUsers(@RequestParam("count") int count) {
        if (count <= 0) {
            return ResponseEntity.badRequest().body("Le nombre d'utilisateurs à créer doit être supérieur à 0.");
        }

        try {
            userService.createMultipleUsers(count); // Implémente cette méthode dans UserService
            return ResponseEntity.ok().body(count + " utilisateurs ont été créés avec succès.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création des utilisateurs : " + e.getMessage());
        }
    }
}
