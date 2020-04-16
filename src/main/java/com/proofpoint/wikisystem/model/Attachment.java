package com.proofpoint.wikisystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Attachment extends Component {

    private String filename;
    private String contents;

    private Attachment(final Builder builder) {
        this.filename = builder.filename;
        this.contents = builder.contents;
        this.owner = builder.owner;
        this.accessMap = new HashMap<>();
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public void create() {
        System.out.println("Creating Attachment");
    }

    public void read() {
        System.out.println("Reading Page");
    }

    public void delete() {
        System.out.println("Deleting Attachment");
    }

    public void update() {
        System.out.println("Updating Attachment");
    }

    /** We may use lombok builder annotation to generate this and reduce boiler plate code as well.
     * Writing this intentionally to demonstrate builder pattern.
     */
    public static class Builder {
        private User owner;
        private String filename;
        private String contents;

        private Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withFilename(final String filename) {
            this.filename = filename;
            return this;
        }

        public Builder withContents(final String contents) {
            this.contents = contents;
            return this;
        }

        public Builder withOwner(final User owner) {
            this.owner = owner;
            return this;
        }

        public Attachment build() {
            return new Attachment(this);
        }

    }
}
