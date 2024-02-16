package org.example.socialnetworkanalysisnosql.data;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
public class Product {

    @Id @GeneratedValue private Long id;
    private String name;
    private String description;
    private double price;

    @Relationship(type = "BOUGHT", direction = Relationship.Direction.INCOMING)
    private Set<User> boughtBy = new HashSet<>();

    public Product() {
    }

    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" + "name=" + name + ", description=" + description + ", price=" + price + '}';
    }

    public void boughtBy(User user) {
        boughtBy.add(user);
    }
}
