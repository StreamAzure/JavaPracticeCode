package com.stream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.stream.filter")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
