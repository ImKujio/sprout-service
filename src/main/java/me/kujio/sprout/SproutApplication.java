package me.kujio.sprout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SproutApplication {

    public static void main(String[] args) {
        SpringApplication.run(SproutApplication.class, args);
        System.out.println("------------------launch complete---------------------");
    }

}
