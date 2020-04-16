package com.proofpoint.wikisystem.controller;

import com.proofpoint.wikisystem.model.User;
import com.proofpoint.wikisystem.payload.CreateUserDto;
import com.proofpoint.wikisystem.payload.UpdateUserDto;
import com.proofpoint.wikisystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.proofpoint.wikisystem.util.Constants.STATUS_FAILED_WITH_MESSAGE;
import static com.proofpoint.wikisystem.util.Constants.STATUS_SUCCESS;

@Slf4j
@RestController
@RequestMapping("/wikisystem/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody final CreateUserDto payload) {

        try {
            log.info("Received request to create user");
            log.info("Payload:" + payload.toString());
            userService.create(payload.getUserId(), payload.getUserName());
            return new ResponseEntity<>(STATUS_SUCCESS, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(STATUS_FAILED_WITH_MESSAGE + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<User> read(@RequestParam final String userID) {
        log.info("Received request to read user");
        User output = userService.read(userID);

        if (output != null) {
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(output, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<String> update(@RequestParam final String userId, @RequestBody final UpdateUserDto payload) {
        log.info("Received request to update user");
        String output = userService.update(userId, payload);

        if (output != null) {
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(output, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> delete(@RequestParam final String userId) {
        log.info("Received request to delete user");

        if (userService.delete(userId)) {
            return new ResponseEntity<>("Page deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Page not found", HttpStatus.NOT_FOUND);
        }
    }

}
