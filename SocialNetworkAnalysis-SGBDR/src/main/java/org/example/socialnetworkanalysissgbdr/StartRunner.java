package org.example.socialnetworkanalysissgbdr;

import lombok.AllArgsConstructor;
import org.apache.catalina.filters.ExpiresFilter;
import org.example.socialnetworkanalysissgbdr.data.Product;
import org.example.socialnetworkanalysissgbdr.data.User;
import org.example.socialnetworkanalysissgbdr.services.ProductService;
import org.example.socialnetworkanalysissgbdr.services.UserService;
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
    }

}
