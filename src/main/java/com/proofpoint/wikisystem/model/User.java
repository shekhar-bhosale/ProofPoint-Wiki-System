package com.proofpoint.wikisystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Collaborator {

    private String username;

    private User(Builder builder) {
        this.Id = builder.ID;
        this.username = builder.username;
    }

    public String getId() {
        return this.Id;
    }

    public void create() {
        System.out.println("Creating User");
    }

    public void read() {
        System.out.println("Reading Page");
    }

    public void delete() {
        System.out.println("Deleting User");
    }

    public void update() {
        System.out.println("Updating User");
    }

    public static class Builder {
        private String ID;
        private String username;

        private Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withID(String ID) {
            this.ID = ID;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
