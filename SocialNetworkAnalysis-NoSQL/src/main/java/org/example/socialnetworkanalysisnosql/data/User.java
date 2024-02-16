package org.example.socialnetworkanalysisnosql.data;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
public class User {

    @Id @GeneratedValue private Long id;
    private String name;
    @Relationship(type = "BOUGHT", direction = Relationship.Direction.OUTGOING)
    private Set<Product> boughtProducts = new HashSet<>();

    @Relationship(type = "FOLLOWERS", direction = Relationship.Direction.INCOMING)
    private Set<User> followers = new HashSet<>();

    @Relationship(type = "FOLLOWERS", direction = Relationship.Direction.OUTGOING)
    private Set<User> follows = new HashSet<>();

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public void buy(Product product) {
        boughtProducts.add(product);
    }

    public void follow(User user) {
        follows.add(user);
    }

    public void followedBy(User user) {
        followers.add(user);
    }

    public int getFollowerCount() {
        return followers.size();
    }
}
