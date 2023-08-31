package org.example.controller;
import org.example.model.hello;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@CrossOrigin
public class HelloWorldController {

    @GetMapping("/helloworld")
    public hello
    helloWorld() {
        return new hello();
    }
}
