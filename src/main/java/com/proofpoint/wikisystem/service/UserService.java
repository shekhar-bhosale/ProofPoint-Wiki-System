package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.model.User;
import com.proofpoint.wikisystem.payload.UpdateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Scope("singleton")
public class UserService {

    private Map<String, User> users = new HashMap<>();

    public void create(final String ID, final String username) {
        log.info("Creating user with userID:" + ID);
        User user = User.Builder
                .newInstance()
                .withID(ID)
                .withUsername(username)
                .build();
        log.info("User created:" + user.toString());
        users.put(ID, user);

    }

    public User read(final String userID) {
        log.info("Checking if user " + userID + " exists in system.");
        return users.getOrDefault(userID, null);
    }

    public String update(final String teamId, final UpdateUserDto updateArgs) {
        if (users.containsKey(teamId)) {
            User user = users.get(teamId);
            if (!updateArgs.getUserName().isEmpty()) {
                user.setUsername(updateArgs.getUserName());
            }
            return "Successfully updated user";
        } else {
            return "User not found";
        }
    }

    public boolean delete(final String userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            return true;
        } else {
            return false;
        }
    }
}
