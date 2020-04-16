package com.proofpoint.wikisystem.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.proofpoint.wikisystem.util.Constants.STATUS_FAILED;
import static com.proofpoint.wikisystem.util.Constants.STATUS_SUCCESS;

@Getter
@Setter
@Slf4j
public class Team extends Collaborator {

    private boolean isAdmin;
    private List<User> members;

    private Team(Builder builder) {
        this.Id = builder.Id;
        this.isAdmin = builder.isAdmin;
        this.members = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Team{" +
                "isAdmin=" + isAdmin +
                ", members=" + members +
                ", Id='" + Id + '\'' +
                '}';
    }

    public String getId() {
        return this.Id;
    }

    public void create() {
        System.out.println("Creating team");
    }

    public void read() {
        System.out.println("Reading Page");
    }

    public void delete() {
        System.out.println("Deleting team");
    }

    public void update() {
        System.out.println("Updating team");
    }

    public String addMember(User user) {
        try {
            members.add(user);
            return STATUS_SUCCESS;
        } catch (RuntimeException e) {
            log.info("Exception when adding member to team:" + e.getMessage());
            return STATUS_FAILED;
        }
    }

    public static class Builder {
        private String Id;
        private boolean isAdmin;

        private Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withID(String Id) {
            this.Id = Id;
            return this;
        }

        public Builder withIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public Team build() {
            return new Team(this);
        }

    }
}
