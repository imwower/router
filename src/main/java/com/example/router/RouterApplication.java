package com.example.router;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RouterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RouterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Router router = new Router();
        router.addRoute("/user/**");
        router.addRoute("/user");
        router.addRoute("/user/login");
        router.addRoute("/user/{userId}");
        router.addRoute("/equipment/**");
        router.addRoute("/user/bg/**");

        System.out.println("OK");

        String route = router.matchRoute("/user/bg/**");
        System.out.println(route);
        route = router.matchRoute("/user/123");
        System.out.println(route);
        route = router.matchRoute("/user");
        System.out.println(route);
    }
}
