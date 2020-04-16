package com.proofpoint.wikisystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wikisystem")
public class WikiSystemController {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> welcomeMessage() {
        log.info("Welcome page");
        String message = "Welcome to WikiSystem";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
