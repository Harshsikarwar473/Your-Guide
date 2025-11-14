package com.YourGuide.YourGuide.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController

public class TesterController {
    @GetMapping
    public String message(){

       return "I am Started";
    }


}
