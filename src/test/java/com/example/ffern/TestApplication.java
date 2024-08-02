package com.example.ffern;

import com.example.Application;
import org.springframework.boot.SpringApplication;

public class TestApplication {

    public static void main(String[] args) {

        SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
