package org.example.socialnetworkanalysissgbdr.repos;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.example.socialnetworkanalysissgbdr.data.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findByName(String s);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Product p WHERE p.id IN (SELECT p.id FROM Product p LIMIT 100000)", nativeQuery = true)
    void dumpLimit();
}
