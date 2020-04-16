package com.proofpoint.wikisystem.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Setter
@Getter
@Slf4j
public class Page extends Component {

    private String pageID;
    private String parentPageID;
    private List<Attachment> attachments;
    private String content;

    private Page(Builder builder) {
        this.owner = builder.owner;
        this.pageID = builder.pageID;
        this.parentPageID = builder.parentPageID;
        this.content = builder.content;
        this.accessMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageID='" + pageID + '\'' +
                ", parentPageID='" + parentPageID + '\'' +
                ", attachments=" + attachments +
                ", content='" + content + '\'' +
                ", owner=" + owner +
                ", accessMap=" + accessMap +
                '}';
    }

    public User getOwner() {
        return this.owner;
    }

    public void read() {
        System.out.println("Reading Page");
    }

    public void create() {
        System.out.println("Creating Page");
    }

    public void delete() {
        System.out.println("Deleting Page");
    }

    public void update() {
        System.out.println("Updating Page");
    }

    public void addAttachment(Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        attachments.add(attachment);
    }


    public static class Builder {
        private User owner;
        private String pageID;
        private String parentPageID;
        private String content;

        private Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withPageID(final String pageID) {
            this.pageID = pageID;
            return this;
        }

        public Builder withParentPageID(final String parentPageID) {
            this.parentPageID = parentPageID;
            return this;
        }

        public Builder withOwner(final User owner) {
            this.owner = owner;
            return this;
        }

        public Builder withContent(final String content) {
            this.content = content;
            return this;
        }


        public Page build() {
            return new Page(this);
        }

    }
}
